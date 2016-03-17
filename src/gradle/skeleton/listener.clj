(clojure.core/ns skeleton.listener
  (:gen-class :implements [javax.servlet.ServletContextListener]))
(do
  (clojure.core/defn -contextInitialized [my-arg1 my-arg2]
    nil
    (clojure.core/let [my-arg3
      (clojure.core/let [my-arg4
        (do
          (clojure.core/require (quote skeleton.web))
          (clojure.core/resolve (quote skeleton.web/handler)))]
        (clojure.core/fn [my-arg5]
          (clojure.core/let [my-arg6
            (.getContextPath ^{:column 27, :line 127, :tag javax.servlet.http.HttpServletRequest}
              (:servlet-request my-arg5))]
            (my-arg4
              (clojure.core/assoc my-arg5 :context my-arg6 :path-info
                (clojure.core/-> (:uri my-arg5)
                  (clojure.core/subs (.length my-arg6))
                  clojure.core/not-empty (clojure.core/or "/")))))))
      service-method1
      (do
        (clojure.core/require (quote ring.util.servlet))
        (clojure.core/resolve (quote ring.util.servlet/make-service-method)))
      my-arg7 (service-method1 my-arg3)]
      (clojure.core/alter-var-root
        (do
          (clojure.core/require (quote skeleton.servlet))
          (clojure.core/resolve (quote skeleton.servlet/service-method)))
        (clojure.core/constantly my-arg7))))
  (clojure.core/defn -contextDestroyed [my-arg1 my-arg2] nil))
