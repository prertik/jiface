(ns clojang.types
  (:require [clojang.core :as clojang]
            [clojang.util :as util])
  (:import [com.ericsson.otp.erlang
             OtpErlangAtom
             OtpErlangBoolean
             OtpErlangBinary
             OtpErlangBitstr
             OtpErlangByte
             OtpErlangChar
             OtpErlangDouble
             OtpErlangFloat
             OtpErlangFun
             OtpErlangInt
             OtpErlangList
             OtpErlangList$SubList
             OtpErlangLong
             OtpErlangMap
             OtpErlangObject
             OtpErlangPid
             OtpErlangPort
             OtpErlangRef
             OtpErlangShort
             OtpErlangString
             OtpErlangTuple
             OtpErlangUInt
             OtpErlangUShort])
  (:refer-clojure :exclude [atom boolean byte char char? double float float?
                            int list list? long map map? short string?]))

;;; Constructor aliases

(defn undefined
  ""
  []
  (clojang/->erlang nil))

(defn atom
  ""
  [arg]
  (clojang/->erlang arg))

(defn boolean
  ""
  [arg]
  (clojang/->erlang arg))

(defn tuple
  ""
  [arg]
  (clojang/->erlang arg))

(defn list
  ""
  ([]
    (clojang/->erlang '()))
  ([arg]
    (clojang/->erlang arg)))

(defn char
  ""
  [arg]
  (clojang/->erlang arg))

(defn string
  ""
  [arg]
  (clojang/->erlang arg))

(defn long
  ""
  [arg]
  (clojang/->erlang arg))

(defn byte
  ""
  [arg]
  (clojang/->erlang arg))

(defn char
  ""
  [arg]
  (clojang/->erlang arg))

(defn int
  ""
  [arg]
  (clojang/->erlang arg))

(defn short
  ""
  [arg]
  (clojang/->erlang arg))

(defn uint
  ""
  [arg]
  (clojang/->erlang arg))

(defn ushort
  ""
  [arg]
  (clojang/->erlang arg))

(defn float
  ""
  [arg]
  (clojang/->erlang arg))

(defn double
  ""
  [arg]
  (clojang/->erlang arg))

(defn map
  ""
  [arg]
  (clojang/->erlang arg))

;;; Predicates

(defn undefined?
  ""
  [arg]
  (and (instance? OtpErlangAtom arg)
       (= (clojang/->clojure arg) nil)))

(defn atom?
  ""
  [arg]
  (instance? OtpErlangAtom arg))

(defn boolean?
  ""
  [arg]
  (instance? OtpErlangBoolean arg))

(defn binary?
  ""
  [arg]
  (instance? OtpErlangBinary arg))

(defn bitstr?
  ""
  [arg]
  (instance? OtpErlangBitstr arg))

(defn byte?
  ""
  [arg]
  (instance? OtpErlangByte arg))

(defn tuple?
  ""
  [arg]
  (instance? OtpErlangTuple arg))

(defn list?
  ""
  [arg]
  (instance? OtpErlangList arg))

(defn char?
  ""
  [arg]
  (instance? OtpErlangChar arg))

(defn string?
  ""
  [arg]
  (instance? OtpErlangString arg))

(defn long?
  ""
  [arg]
  (instance? OtpErlangLong arg))

(defn int?
  ""
  [arg]
  (instance? OtpErlangInt arg))

(defn short?
  ""
  [arg]
  (instance? OtpErlangShort arg))

(defn uint?
  ""
  [arg]
  (instance? OtpErlangUInt arg))

(defn ushort?
  ""
  [arg]
  (instance? OtpErlangUShort arg))

(defn float?
  ""
  [arg]
  (instance? OtpErlangFloat arg))

(defn double?
  ""
  [arg]
  (instance? OtpErlangDouble arg))

(defn map?
  ""
  [arg]
  (instance? OtpErlangMap arg))

(defn fun?
  ""
  [arg]
  (instance? OtpErlangFun arg))

(defn sublist?
  ""
  [arg]
  (instance? OtpErlangList$SubList arg))

(defn object?
  ""
  [arg]
  (instance? OtpErlangObject arg))

(defn pid?
  ""
  [arg]
  (instance? OtpErlangPid arg))

(defn port?
  ""
  [arg]
  (instance? OtpErlangPort arg))

(defn ref?
  ""
  [arg]
  (instance? OtpErlangRef arg))
