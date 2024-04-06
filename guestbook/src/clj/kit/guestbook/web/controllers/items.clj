(ns kit.guestbook.web.controllers.items
  (:require
   [clojure.tools.logging :as log]
   [kit.guestbook.web.routes.utils :as utils]
   [ring.util.http-response :as http-response]))

(defn add-item!
  [{{:strs [name]} :form-params :as request}]
  (log/debug "saving item" name)
  (let [{:keys [query-fn]} (utils/route-data request)]    
    (try      
      (if (empty? name)
       (cond-> (http-response/found "/")
         (empty? name)
         (assoc-in [:flash :errors :name] "name is required"))
        (do
          (query-fn :add-item! {:name name})
          (http-response/found "/")))
      (catch Exception e
        (log/error e "failed to save item!")
        (-> (http-response/found "/")
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))