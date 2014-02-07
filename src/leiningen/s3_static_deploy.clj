(ns leiningen.s3-static-deploy
  (:require [aws.sdk.s3 :as s3]
            [me.kanej.s3-sync :as sync]
            [clojure.data.json :as json]
            [clojure.java.data :as java])
  (:import com.amazonaws.auth.BasicAWSCredentials
           com.amazonaws.services.s3.AmazonS3Client
           com.amazonaws.ClientConfiguration
           com.amazonaws.services.s3.model.BucketWebsiteConfiguration))

(defn- s3-client*
  [cred]
  (let [client-configuration (ClientConfiguration.)]
    (when-let [conn-timeout (:conn-timeout cred)]
      (.setConnectionTimeout client-configuration conn-timeout))
    (when-let [socket-timeout (:socket-timeout cred)]
      (.setSocketTimeout client-configuration socket-timeout))
    (when-let [max-retries (:max-retries cred)]
      (.setMaxErrorRetry client-configuration max-retries))
    (when-let [max-conns (:max-conns cred)]
      (.setMaxConnections client-configuration max-conns))
    (let [aws-creds (BasicAWSCredentials. (:access-key cred) (:secret-key cred))
          client    (AmazonS3Client. aws-creds client-configuration)]
      (when-let [endpoint (:endpoint cred)]
        (.setEndpoint client endpoint))
      client)))

(def ^{:private true}
  s3-client
  (memoize s3-client*))

(defn bucket-exists-in-account? [cred name]
  (some #{name} (map :name (s3/list-buckets cred))))

(defn update-bucket-policy
  "Create a new S3 bucket with the supplied name."
  [cred ^String name policy]
  (let [policy-json (json/write-str policy)]
    (.setBucketPolicy (s3-client cred) name policy-json)))


(defn update-bucket-website-configuration [cred name configration]
  (.setBucketWebsiteConfiguration (s3-client cred)
                                  name
                                  (java/to-java BucketWebsiteConfiguration configration)))

(defn public-policy [bucket]
  {:Version "2012-10-17"
   :Statement [{:Sid "PublicReadGetObject"
                :Effect "Allow"
                :Principal { :AWS "*"}
                :Action ["s3:GetObject"],
                :Resource [(str "arn:aws:s3:::" bucket "/*")]}]})

(defn s3-static-deploy [project & args]
  "Deploy a local directory as a static website in AWS S3."
  (let [aws (:aws project)
        credentials (select-keys aws [:access-key :secret-key])
        {bucket :bucket local-root :local-root} (:s3-static-deploy aws)]

    (println (str "Deploying " local-root " to " bucket))
    (when-not (bucket-exists-in-account? credentials bucket)
      (s3/create-bucket credentials bucket))

    (update-bucket-website-configuration credentials bucket {:indexDocumentSuffix "index.html"})
    (update-bucket-policy credentials bucket (public-policy bucket))
    (sync/sync-to-s3 credentials local-root bucket)))
