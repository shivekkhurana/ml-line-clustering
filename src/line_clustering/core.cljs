(ns line-clustering.core
  (:require [alandipert.storage-atom :refer [local-storage]]
            [goog.string :as gstring]
            [goog.string.format]))

(enable-console-print!)

(def app-state (local-storage (atom {}) :app-state)) ;; saves/loads data from local-storage to app-state atom

(defn generate-apple-features [n]
  (map (fn []
         {:weight (+ 100 (* (rand) 10)) :fruit :apple}) (range n)))

(defn generate-orange-features [n]
  (map (fn []
         {:weight (+ 108 (* (rand) 8)) :fruit :orange}) (range n)))

(defn generate-training-data []
  (if (> (count (get-in @app-state [:training-vectors :apples])) 0)
    (println "Data loaded from local-storage.")
    (do
      (println "Generating data.")
      (swap! app-state assoc
             :initial-guess 0.5 ; vertical line  x = 0.5
             :improvement 0.05
             :training-vectors (concat (generate-apple-features 3200)
                                       (generate-orange-features 3200))
             :testing-vectors (concat (generate-apple-features 400)
                                      (generate-orange-features 400))
             :real-vectors (concat (generate-apple-features 24)
                                   (generate-orange-features 24))))))

(defn clear-local-storage []
  (reset! app-state {}))

;; Classifier function
(defn which-fruit [{:keys [weight]}]
  "If the weight is on the right of the vertical line, then it's orange, else apple"
  (let [initial-guess (get @app-state :initial-guess)]
    (if (> weight initial-guess) :orange :apple)))

(defn train-model []
  (let [training-vectors (get @app-state :training-vectors)
        improvement (get @app-state :improvement)]
    (run! (fn [v]
            (let [actual-fruit (get v :fruit)
                  actual-weight (get v :weight)
                  computed-fruit (which-fruit v)
                  initial-guess (get @app-state :initial-guess)]
              (if (= actual-fruit computed-fruit)
                (println (gstring/format "Do nothing as the model f(x) = %.3fx is doing just fine." initial-guess))
                (do
                  (println "Improving guess")
                  (if (> actual-weight initial-guess)
                    (swap! app-state update-in [:initial-guess] + improvement) ; move the line towards right
                    (swap! app-state update-in [:initial-guess] - improvement) ; move the line towards left 
                    )
                  (println (gstring/format "Now guess is f(x) = %.3fx" initial-guess)))))) training-vectors)))

(defn show-model []
  (println "The model is f(x) = x * " (get @app-state :initial-guess)))

(defn test-model []
  (println "Testing the model.")
  (let [results (map (fn [v]
                       (let [computed (which-fruit v)
                             actual (get v :fruit)]
                         ;; check if computed fruit is equal to actual fruit
                         (println "Actual: " actual "; Computed : " computed)
                         (= computed actual))) (get @app-state :testing-vectors))]
    (println "Accuracy is " (/ (count (filter true? results)) (count results)))))

;; (clear-local-storage)
;; (generate-training-data)
;; (train-model)
;; (show-model)
(test-model)

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
