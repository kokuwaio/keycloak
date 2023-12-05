export VERSION=$(mvn help:evaluate -Dexpression=version.org.keycloak -DforceStdout -q)
export REDHAT_RUNTIME=quay.io/keycloak/keycloak:$VERSION
export TEMURIN_RUNTIME=docker.io/kokuwaio/keycloak:$VERSION
export TEMURIN_IMPORT=docker.io/kokuwaio/keycloak:$VERSION-import
export DISTROLESS_RUNTIME=docker.io/kokuwaio/keycloak:$VERSION-distroless
export DISTROLESS_IMPORT=docker.io/kokuwaio/keycloak:$VERSION-distroless-import

docker pull --quiet=true $REDHAT_RUNTIME &
docker pull --quiet=true $TEMURIN_RUNTIME &
docker pull --quiet=true $TEMURIN_IMPORT &
docker pull --quiet=true $DISTROLESS_RUNTIME &
docker pull --quiet=true $DISTROLESS_IMPORT &
wait

printf "\n| image                                                | uncompressed      | compressed        |\n"
printf "|------------------------------------------------------|-------------------|-------------------|\n"
printf "| %-52s | %'11d bytes | %'11d bytes |\n" $REDHAT_RUNTIME     $(docker image inspect $REDHAT_RUNTIME     --format='{{.Size}}') $(docker manifest inspect -v $REDHAT_RUNTIME     | jq '.[] | select(.Descriptor.platform.architecture == "amd64").OCIManifest.layers[].size' | tr '\n' '+' | cat - <(echo "0") | bc)
printf "| %-52s | %'11d bytes | %'11d bytes |\n" $TEMURIN_RUNTIME    $(docker image inspect $TEMURIN_RUNTIME    --format='{{.Size}}') $(docker manifest inspect -v $TEMURIN_RUNTIME    | jq '.[] | select(.Descriptor.platform.architecture == "amd64").OCIManifest.layers[].size' | tr '\n' '+' | cat - <(echo "0") | bc)
printf "| %-52s | %'11d bytes | %'11d bytes |\n" $TEMURIN_IMPORT     $(docker image inspect $TEMURIN_IMPORT     --format='{{.Size}}') $(docker manifest inspect -v $TEMURIN_IMPORT     | jq '.[] | select(.Descriptor.platform.architecture == "amd64").OCIManifest.layers[].size' | tr '\n' '+' | cat - <(echo "0") | bc)
printf "| %-52s | %'11d bytes | %'11d bytes |\n" $DISTROLESS_RUNTIME $(docker image inspect $DISTROLESS_RUNTIME --format='{{.Size}}') $(docker manifest inspect -v $DISTROLESS_RUNTIME | jq '.[] | select(.Descriptor.platform.architecture == "amd64").OCIManifest.layers[].size' | tr '\n' '+' | cat - <(echo "0") | bc)
printf "| %-52s | %'11d bytes | %'11d bytes |\n" $DISTROLESS_IMPORT  $(docker image inspect $DISTROLESS_IMPORT  --format='{{.Size}}') $(docker manifest inspect -v $DISTROLESS_IMPORT  | jq '.[] | select(.Descriptor.platform.architecture == "amd64").OCIManifest.layers[].size' | tr '\n' '+' | cat - <(echo "0") | bc)
