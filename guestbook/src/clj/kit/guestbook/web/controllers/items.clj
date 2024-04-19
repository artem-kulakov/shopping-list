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

(defn toggle-item-complete!
  [{{:strs [id complete]} :form-params :as request}]
  (log/debug "updating item complete" id complete)
  (let [{:keys [query-fn]} (utils/route-data request)]
    (try
      (if (or (empty? id) (empty? complete))
        (cond-> (http-response/found "/")
          (empty? complete)
          (assoc-in [:flash :errors :status] "complete is required"))
        (do
          (query-fn :update-item-complete! {:id id :complete (= complete "0")})
          (http-response/found "/")))
      (catch Exception e
        (log/error e "failed to update item!")
        (-> (http-response/found "/")
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))

(defn sort-items!
  [{{:strs []} :form-params :as request}]
  (log/debug "sorting items")
  (let [{:keys [query-fn]} (utils/route-data request)]
    (try
      (query-fn :sort-items! {})
      (http-response/found "/")
      (catch Exception e
        (log/error e "failed to sort items!")
        (-> (http-response/found "/")
            (assoc :flash {:errors {:unknown (.getMessage e)}}))))))
