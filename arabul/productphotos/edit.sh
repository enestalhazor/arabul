#!/bin/bash

i=0

# Find only image files and sort them
find . -maxdepth 1 -type f \( -iname "*.jpg" -o -iname "*.jpeg" -o -iname "*.png" \) \
    -print0 | sort -z | while IFS= read -r -d '' file; do

    # Stop after 50 files
    if [ $i -ge 50 ]; then
        break
    fi

    mv "$file" "./$i.jpg"
    ((i++))
done

echo "Renamed $i files."

