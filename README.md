# jiface
[![Build Status][travis-badge]][travis]
[![Dependencies Status][deps-badge]][deps]
[![Clojars Project][clojars-badge]][clojars]

*Erlang's JInterface in Idiomatic Clojure*

[![Project logo][logo]][logo-large]


#### Contents

* [Introduction](#introduction-)
* [Dependencies](#dependencies-)
* [Building](#building-)
* [Documentation](#documentation-)
* [Usage](#usage-)
* [Running Tests](#running-tests-)
* [Erlang, Clojure, and JInterface](#erlang-clojure-and-jinterface-)
* [License](#license-)


## Introduction [&#x219F;](#contents)

This project provides a solution to the Clojure
[JInterface Problem](https://github.com/clojang/jiface/wiki/The-JInterface-Problem).
While JInterface is an invaluable tool for projects that need to have JVM and
Erlang VM languages communicating with each other, it is rather verbose and
cumbersome to do so in Clojure. The syntactical burden is often enough to
discourage experimentation and play (essential ingredients for innovation).
The primary goal of jiface is to make it easier to write Clojure programs that
need to communicate with Erlang nodes (including LFE and Elixir).

For a comparison of JInterface, the low-level `jiface` API, and the high-level
`clojang` API, see
[the APIs summary](http://clojang.github.io/jiface/current/05-apis.html) page.


## Changes [&#x219F;](#contents)

For details on breaking changes made between jiface releases, see
[the changes](http://clojang.github.io/jiface/current/80-changes.html) page.


## Dependencies [&#x219F;](#contents)

* Java
* Erlang (`epmd` in particular)
* lein

The default (and tested) version combinations are as follows:

| jiface | JInterface | Erlang Release | Erlang Version (erts) |
|--------|------------|----------------|-----------------------|
| 0.4.0  | 1.7.1      | 19.2, 19.3     | 8.2, 8.3              |
| 0.3.0  | 1.7.1      | 19.2           | 8.2                   |
| 0.2.0  | 1.7.1      | 19.2           | 8.2                   |
| 0.2.0  | 1.7.1      | 19.1           | 8.1                   |
| 0.1.0  | 1.6.1      | 18.3           | 7.3                   |
| 0.1.0  | 1.6.1      | 18.2           | 7.2                   |

While other version combination may work (and existing versions may be updated
to work with different onces), those are the only ones which are supported.


## Building [&#x219F;](#contents)

``rebar3`` is used for the top-level builds of the project. It runs ``lein``
under the covers in order to build the Clojure code and create the
jiface``.jar`` file. As such, to build everything -- LFE, Erlang, and Clojure
-- you need only do the following:

* ``rebar3 compile``

If you wish to build your own JInterface ``.jar`` file and not use the one
we've uploaded to Clojars, you'll need to follow the instructions given in the
documentation here:

* [Building JInterface for Clojure](http://clojang.github.io/jiface/current/80-building-jinterface.html)


## Documentation [&#x219F;](#contents)

Project documentation, including jiface API reference docs, Javadocs for
JInterface, and the Erlang JInterface User's Guide, is available here:

* [http://clojang.github.io/jiface/current/](http://clojang.github.io/jiface/current/)

Of particular note:

* [jiface User's Guide](http://clojang.github.io/jiface/current/10-low-level-api.html) - A translation of the *JInterface User's Guide* (Erlang documentation) from Java into Clojure
* [JInterface User's Guide](http://clojang.github.io/jiface/current/erlang/jinterface_users_guide.html) - The JInterface documentation provided in Erlang distributions
* [JInterface Javadocs](http://clojang.github.io/jiface/current/erlang/java) - Javadoc-generated API documentation built from the JInterface source code

High-level API docs:

* [Clojang User's Guide](http://clojang.github.io/clojang/current/10-low-level.html) -
  An adaptation of the *jiface User's Guide* for the mid-level idiomatic Clojure API


## Usage [&#x219F;](#contents)

Using jiface in a project is just like any other Clojure library. Just add the
following to the ``:dependencies`` in your ``project.clj`` file:

[![Clojars Project](https://img.shields.io/clojars/v/clojang/jiface.svg)](https://clojars.org/clojang/jiface)

For the Erlang/LFE side of things, you just need to add the Github URL to your
`rebar.config` file, as with any other rebar-based Erlang VM project.

As for actual code usage, the documentation section provides links to
developer guides and API references, but below is quick example.

Start LFE in distributed mode:

```
$ lfe -sname clojang-lfe
Erlang/OTP 18 [erts-7.3] [source] [64-bit] [smp:4:4] [async-threads:10] ...

   ..-~.~_~---..
  (      \\     )    |   A Lisp-2+ on the Erlang VM
  |`-.._/_\\_.-';    |   Type (help) for usage info.
  |         g (_ \   |
  |        n    | |  |   Docs: http://docs.lfe.io/
  (       a    / /   |   Source: http://github.com/rvirding/lfe
   \     l    (_/    |
    \   r     /      |   LFE v0.11.0-dev (abort with ^G)
     `-E___.-'

(clojang-lfe@mndltl01)>
```

Then start up a jiface Clojure REPL:

```
$ lein repl
```

And paste the following:

```clj
(require '[jiface.otp.messaging :as messaging]
         '[jiface.otp.nodes :as nodes]
         '[jiface.erlang.types :as types]
         '[jiface.erlang.tuple :as tuple-type])
(def node (nodes/node "gurka"))
(def mbox (messaging/mbox node))
(messaging/register-name mbox "echo")
(def msg (into-array
           (types/object)
           [(messaging/self mbox)
            (types/atom "hello, world")]))
(messaging/! mbox "echo" "gurka" (types/tuple msg))
(messaging/receive mbox)
```

When you paste the ``receive`` function, you'll get something like this:

```clj
#object[com.ericsson.otp.erlang.OtpErlangTuple
        0x4c9e3fa6
        "{#Pid<gurka@mndltl01.1.0>,'hello, world'}"]
```

In the LFE REPL, you can send a message to your new Clojure node:

```cl
(lfe@mndltl01)> (! #(echo gurka@mndltl01) `#(,(self) hej!))
#(<0.35.0> hej!)
```

Then back in Clojure, check the sent message and send a response:

```clojure
(def data (messaging/receive mbox))
(def lfe-pid (tuple-type/get-element data 0))
(messaging/! mbox lfe-pid (types/tuple msg))
```

Then, back in LFE, flush the REPL's process mbox to see what has been sent to it:

```cl
(lfe@mndltl01)> (c:flush)
Shell got {<5926.1.0>,'hello, world'}
ok
```


## Running Tests [&#x219F;](#contents)

All the tests may be run with just one command:

```bash
$ rebar3 eunit
```

This will not only run Erlang and LFE unit tests, it also runs the Clojure unit tests for jiface.

**Clojure Test Selectors**

If you would like to be more selective in the types of jiface tests which get
run, you may be interested in reading this section.

The jiface tests use metadata annotations to indicate whether they are unit,
system, or integration tests. to run just the unit tests, you can do any one
of the following, depending upon what you're used to:

```bash
$ lein test
$ lein test :unit
$ lein test :default
```

To run just the system tests:

```bash
$ lein test :system
```

And, similarly, just the integration tests:

```bash
$ lein test :integration
```

To run everything:

```bash
$ lein test :all
```

This is what is used by the ``rebar3`` configuration to run the jiface tests.


## Erlang, Clojure, and JInterface [&#x219F;](#contents)

If you are interested in building your own JInterface ``.jar`` file for use
with a Clojure project, be sure fo check out the
[jinterface-builder Clojang project](https://github.com/clojang/jinterface-builder).
The project `README` has everything you need to get started.


## License [&#x219F;](#contents)

```
Copyright © 2016-2017 Duncan McGreggor

Distributed under the Apache License Version 2.0.
```


<!-- Named page links below: /-->

[travis]: https://travis-ci.org/clojang/jiface
[travis-badge]: https://travis-ci.org/clojang/jiface.png?branch=master
[deps]: http://jarkeeper.com/clojang/jiface
[deps-badge]: http://jarkeeper.com/clojang/jiface/status.svg
[clojars]: https://clojars.org/clojang/jiface
[clojars-badge]: https://img.shields.io/clojars/v/clojang/jiface.svg
[logo]: https://github.com/clojang/resources/blob/master/images/logo-5-250x.png
[logo-large]: https://github.com/clojang/resources/blob/master/images/logo-5-1000x.png
