(defproject s3-static-deploy "0.1.0"
  :description "Leiningen plugin to deploy static websites"
  :url "https://github.com/ThoughtWorksInc/lein-s3-static-deploy"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-aws-s3 "0.3.7"]
                 [me.kanej/s3-sync "0.2.0"]
                 [org.clojure/java.data "0.1.1"]]
  :eval-in-leiningen true
  :min-lein-version "2.0.0"
  :scm {:name "git"
        :url "https://github.com/ThoughtWorksInc/lein-s3-static-deploy"})
