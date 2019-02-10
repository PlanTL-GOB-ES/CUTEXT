#!/bin/sh
#
#shell script to execute freeling
#
cat $1 | docker run -i$([ -t 1 ] && cat t) --rm freeling-cnio:1.0.0 > $2
