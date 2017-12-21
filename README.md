# line-clustering

Baby machine learning problem that clusters fruits on the basis of features.

## Overview

Currently uses a simple vertical line model (x = constant). Randomly generates trainining data and learns to predict if a given fruit is apple or orange on the basis of weight.
With current data generation, an accuracy of 89 % is achieved. All oranges are classified properly, the applese, hmm, not quite.

## Setup

To get an interactive development environment run:

    lein figwheel

and open your browser at [localhost:3449](http://localhost:3449/).
This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    lein clean

To create a production build run:

    lein do clean, cljsbuild once min

And open your browser in `resources/public/index.html`. You will not
get live reloading, nor a REPL. 

## License

Copyright © 2017 Shivek Khurana

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
