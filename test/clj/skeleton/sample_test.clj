(ns skeleton.sample-test
  (:use clojure.test skeleton.test-helper)
  (:require [skeleton.web :as web]))

(deftest dev-mode-test
  (testing "should be true"
    (let [result (web/in-dev?)]
      (is (= true result)))))
