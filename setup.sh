#!/usr/bin/env bash

REBUILD=false
RUN=false

cur_dir=`pwd`

function build_db_image() {
    echo "Building DB"
    cd "$cur_dir/setup/db" && ./build.sh
}

function build_server_image() {
    echo "Building server"
    cd $cur_dir && docker build -t dojo/hardinv-server:0.0.1 -f setup/app/Dockerfile .
}

function build() {
    build_db_image
    build_server_image
}

function run() {
    cd "$cur_dir/setup" && docker compose up -d
}

while true; do
        case "$1" in
                -b | --build) REBUILD=true; shift ;;
                -r | --run) RUN=true; shift ;;
                * ) break ;;
        esac
done

if [[ "$REBUILD" = true ]]; then
    build
fi

if [[ "$RUN" = true ]]; then
    run
fi