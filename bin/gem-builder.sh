#!/usr/bin/env bash
pushd ../

# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/red-gem.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/green-gem.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/blue-gem.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/yellow-gem.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/white-gem.svg

# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/red-crash.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/green-crash.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/blue-crash.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/yellow-crash.svg
# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/white-crash.svg

# qlmanage -t -s 1024 -o ./offline/assets ./offline/src/design/svg/wild.svg

convert -resize 256x256 offline/assets/src/red-gem.png offline/assets/tgt/red-gem.png
convert -resize 256x256 offline/assets/src/green-gem.png offline/assets/tgt/green-gem.png
convert -resize 256x256 offline/assets/src/blue-gem.png offline/assets/tgt/blue-gem.png
convert -resize 256x256 offline/assets/src/yellow-gem.png offline/assets/tgt/yellow-gem.png
convert -resize 256x256 offline/assets/src/white-gem.png offline/assets/tgt/white-gem.png

convert -resize 256x256 offline/assets/src/red-crash.png offline/assets/tgt/red-crash.png
convert -resize 256x256 offline/assets/src/green-crash.png offline/assets/tgt/green-crash.png
convert -resize 256x256 offline/assets/src/blue-crash.png offline/assets/tgt/blue-crash.png
convert -resize 256x256 offline/assets/src/yellow-crash.png offline/assets/tgt/yellow-crash.png
convert -resize 256x256 offline/assets/src/white-crash.png offline/assets/tgt/white-crash.png

convert -resize 256x256 offline/assets/src/wild.png offline/assets/tgt/wild.png
