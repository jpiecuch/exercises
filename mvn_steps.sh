#!/usr/bin/env bash

step=$1
commit=$2
docker_user=$3
docker_password=$4
modules=(web)

build() {
    ./mvnw clean package
}

component_tests() {
    ./mvnw verify -Pcomponent-test
}

build_image() {
    ./mvnw dockerfile:build -Ddockerfile.tag=$2
}

push_image() {
    ./mvnw dockerfile:push -Ddockerfile.username=$3 -Ddockerfile.password=$4
}

iterate() {
    step=$1
    commit=$2
    docker_user=$3
    docker_password=$4
    for i in "${modules[@]}"
    do
        $step $i $commit $docker_user $docker_password
    done
}

iterate $step $commit $docker_user $docker_password