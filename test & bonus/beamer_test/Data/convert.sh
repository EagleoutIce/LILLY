#!/bin/bash

for a in $(ls | grep .svg); do
	inkscape -D -z --file=$a --export-pdf=${a/.*/}.pdf --export-latex
done
