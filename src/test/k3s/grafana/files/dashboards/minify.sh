#!/bin/sh
set +x;
set +e;

for file in *.json; 
do
  cat $file \
    | jq 'del(.iteration)' \
    | jq 'del(.style)' \
    | jq 'del(.id)' \
    | jq 'if (.annotations.list[0].builtIn == 1) then del(.annotations.list[0]) else . end' \
    | jq 'if (.fiscalYearStartMonth == 0) then del(.fiscalYearStartMonth) else . end' \
    | jq 'if (.liveNow == false) then del(.liveNow) else . end' \
    | jq '.editable=false' \
    | jq '.version=1' \
    | jq '.graphTooltip=2' \
    | jq '.time.to="now"' \
    | jq 'walk(if type == "array" then map(select(. != null)) elif type == "object" then with_entries(select(.value != null and .value != "" and .value != [] and .value != {})) else . end)' \
    | jq --sort-keys \
    > $file.tmp
  rm $file
  mv $file.tmp $file
done;
