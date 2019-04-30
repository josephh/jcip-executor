#!/bin/bash

for i in `seq -f "%04g" 0 9999`;
do
     str=`echo try $i`
     echo $str
     echo nc localhost 59090
done
