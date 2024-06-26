(ns kit.guestbook.web.routes.pages
  (:require
   [kit.guestbook.web.controllers.items :as items]
   [kit.guestbook.web.middleware.exception :as exception]
   [kit.guestbook.web.pages.layout :as layout]
   [kit.guestbook.web.routes.utils :as utils]
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                    {:status 403
                     :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(defn home [{:keys [flash] :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)]
    (layout/render request "home.html" {:items (query-fn :get-items {})
                                        :errors (:errors flash)})))

;; Routes
(defn page-routes [_opts]
  [["/" {:get home}]
   ["/add-item" {:post items/add-item!}]
   ["/toggle-item-complete" {:post items/toggle-item-complete!}]
   ["/sort-items" {:post items/sort-items!}]])

(defn route-data [opts]
  (merge opts
         {:middleware [;; Default middleware for pages
                       (wrap-page-defaults)
                       ;; query-params & form-params
                       parameters/parameters-middleware
                       ;; encoding response body
                       muuntaja/format-response-middleware
                       ;; exception handling
                       exception/wrap-exception]}))

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer!)
  [base-path (route-data opts) (page-routes opts)])