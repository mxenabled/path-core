#!/bin/bash

# Fixes an illegal byte sequence issue on MacOS
export LC_ALL=C

function usage {
  echo "Usage: $0 [directory] [-d]"
  echo
  echo "Where:"
  echo "   -d perform a dry run (default: true)"
  echo
  echo "Example 1 (dry run):"
  echo "   $0 /some/cool/directory"
  echo "Example 2 (live run):"
  echo "   $0 /some/cool/directory -d false"
  exit 0
}

if [ -z "$1" ]; then
  usage
else
  ROOT_DIR=$1
  shift
fi

DRY_RUN=true

while getopts ":d:" OPTION; do
  case $OPTION in
  d)
    if [ "$OPTARG" = false ] || [ "$OPTARG" = 0 ]; then
      DRY_RUN=false
    elif [ "$OPTARG" = true ] || [ "$OPTARG" = 1 ]; then
      DRY_RUN=true
    else
      echo "Invalid option passed to -d: $OPTARG"
      echo -e "Acceptable values are: true, false, 1, 0\n"
      usage
      exit 1
    fi
    ;;
  \?)
    usage
    exit 1
    ;;
  :)
    usage
    exit 1
    ;;
  esac
done

if $DRY_RUN; then
  echo "Doing a dry run. No files will be modified."
else
  echo "Doing a live run. Files may be modified."
fi

# Find & Replace all imports
function process_file {
  local sdk_version="18.+"
  local facilities_version="10.+"
  local web_version="32.+"

  export nl=$'\n'
  local file=$1
  local java_mappings=(
    "com.mx.accessors.AccessorConfiguration([^a-zA-Z])=com.mx.common.accessors.AccessorConfiguration\1"
    "com.mx.accessors.AccessorConnectionBase([^a-zA-Z])=com.mx.common.accessors.AccessorConnectionBase\1"
    "com.mx.accessors.AccessorConnections([^a-zA-Z])=com.mx.common.accessors.AccessorConnections\1"
    "com.mx.accessors.AccessorConnection([^a-zA-Z])=com.mx.common.accessors.AccessorConnection\1"
    "com.mx.accessors.AccessorException([^a-zA-Z])=com.mx.common.accessors.AccessorException\1"
    "com.mx.accessors.AccessorMethodDefinition([^a-zA-Z])=com.mx.common.accessors.AccessorMethodDefinition\1"
    "com.mx.accessors.AccessorResponseStatus([^a-zA-Z])=com.mx.common.accessors.AccessorResponseStatus\1"
    "com.mx.accessors.AccessorResponse([^a-zA-Z])=com.mx.common.accessors.AccessorResponse\1"
    "com.mx.accessors.Accessor([^a-zA-Z])=com.mx.common.accessors.Accessor\1"
    "com.mx.accessors.API([^a-zA-Z])=com.mx.common.accessors.API\1"
    "com.mx.accessors.RootAccessor([^a-zA-Z])=com.mx.common.accessors.RootAccessor\1"
    "com.mx.path.api.connect.messaging.remote.MdxListOfJson([^a-zA-Z])=com.mx.common.remote.MdxListOfJson\1"

    "com.mx.models.Internal([^a-zA-Z])=com.mx.common.models.Internal\1"
    "com.mx.models.MdxBase([^a-zA-Z])=com.mx.common.models.MdxBase\1"
    "com.mx.models.MdxList([^a-zA-Z])=com.mx.common.models.MdxList\1"
    "com.mx.models.MdxWrappable([^a-zA-Z])=com.mx.common.models.MdxWrappable\1"
    "com.mx.models.Warning([^a-zA-Z])=com.mx.common.models.Warning\1"

    "com.mx.serializers.YamlSerializer([^a-zA-Z])=com.mx.common.serialization.ObjectMapYamlDeserializer\1"
    "([^a-zA-Z])YamlSerializer([^a-zA-Z])=\1ObjectMapYamlDeserializer\2"
    "com.mx.adapters.JsonObjectMapDeserializer([^a-zA-Z])=com.mx.common.serialization.ObjectMapJsonDeserializer\1"
    "([^a-zA-Z])JsonObjectMapDeserializer([^a-zA-Z])=\1ObjectMapJsonDeserializer\2"

    "([^a-zA-Z])com.mx.common.process.FaultTolerantExecutionFailureStatus([^a-zA-Z])=\1com.mx.common.accessors.PathResponseStatus\2"
    "([^a-zA-Z])AccessorResponseStatus([^a-zA-Z])=\1PathResponseStatus\2"
    "([^a-zA-Z])AccessorResponseStatus.GATEWAY_TIMEOUT([^a-zA-Z])=\1PathResponseStatus.TIMEOUT\2"
    "([^a-zA-Z])AccessorResponseStatus.SERVICE_UNAVAILABLE([^a-zA-Z])=\1PathResponseStatus.UNAVAILABLE\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_TIMEOUT([^a-zA-Z])=\1PathResponseStatus.TIMEOUT\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_EXECUTION_UNAVAILABLE([^a-zA-Z])=\1PathResponseStatus.UNAVAILABLE\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_LIMIT_EXCEEDED([^a-zA-Z])=\1PathResponseStatus.TOO_MANY_REQUESTS\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.INTERNAL_ERROR([^a-zA-Z])=\1PathResponseStatus.INTERNAL_ERROR\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus([^a-zA-Z])=\1PathResponseStatus\2"

    "com.mx.path.gateway.GatewayException([^a-zA-Z])=com.mx.common.gateway.GatewayException\1"
    "com.mx.common.accessors.AccessorException([^a-zA-Z])=com.mx.common.exception.AccessorException\1"
    "com.mx.common.accessors.ConnectException([^a-zA-Z])=com.mx.common.exception.ConnectException\1"

    "AccessorException\(PathResponseStatus.NOT_IMPLEMENTED\)=AccessorMethodNotImplementedException\(\)"
    "package com.mx.accessors([_a-zA-Z\.]*);$=package com.mx.accessors\1;\nimport com.mx.common.exception.AccessorMethodNotImplementedException;\nimport com.mx.common.gateway.GatewayAPI;\nimport com.mx.common.accessors.API;\nimport com.mx.common.accessors.AccessorConfiguration;\nimport com.mx.common.accessors.Accessor;\nimport com.mx.common.accessors.RootAccessor;\nimport com.mx.common.accessors.AccessorResponse;\nimport com.mx.common.accessors.PathResponseStatus;"
    "package com.mx.models([_a-zA-Z\.]*);$=package com.mx.models\1;\nimport com.mx.common.models.MdxBase;\nimport com.mx.common.models.MdxWrappable;\nimport com.mx.common.models.MdxList;\nimport com.mx.common.models.Internal;"
  )
  local groovy_mappings=(
    "com.mx.accessors.AccessorConfiguration([^a-zA-Z]?)=com.mx.common.accessors.AccessorConfiguration\1"
    "com.mx.accessors.AccessorConnectionBase([^a-zA-Z]?)=com.mx.common.accessors.AccessorConnectionBase\1"
    "com.mx.accessors.AccessorConnections([^a-zA-Z]?)=com.mx.common.accessors.AccessorConnections\1"
    "com.mx.accessors.AccessorConnection([^a-zA-Z]?)=com.mx.common.accessors.AccessorConnection\1"
    "com.mx.accessors.AccessorException([^a-zA-Z]?)=com.mx.common.accessors.AccessorException\1"
    "com.mx.accessors.AccessorMethodDefinition([^a-zA-Z]?)=com.mx.common.accessors.AccessorMethodDefinition\1"
    "com.mx.accessors.AccessorResponseStatus([^a-zA-Z]?)=com.mx.common.accessors.AccessorResponseStatus\1"
    "com.mx.accessors.AccessorResponse([^a-zA-Z]?)=com.mx.common.accessors.AccessorResponse\1"
    "com.mx.accessors.Accessor([^a-zA-Z]?)=com.mx.common.accessors.Accessor\1"
    "com.mx.accessors.API([^a-zA-Z]?)=com.mx.common.accessors.API\1"
    "com.mx.accessors.RootAccessor([^a-zA-Z]?)=com.mx.common.accessors.RootAccessor\1"
    "com.mx.path.api.connect.messaging.remote.MdxListOfJson([^a-zA-Z]?)=com.mx.common.remote.MdxListOfJson\1"

    "com.mx.models.Internal([^a-zA-Z]?)=com.mx.common.models.Internal\1"
    "com.mx.models.MdxBase([^a-zA-Z]?)=com.mx.common.models.MdxBase\1"
    "com.mx.models.MdxList([^a-zA-Z]?)=com.mx.common.models.MdxList\1"
    "com.mx.models.MdxWrappable([^a-zA-Z]?)=com.mx.common.models.MdxWrappable\1"
    "com.mx.models.Warning([^a-zA-Z]?)=com.mx.common.models.Warning\1"

    "com.mx.serializers.YamlSerializer([^a-zA-Z])=com.mx.common.serialization.ObjectMapYamlDeserializer\1"
    "([^a-zA-Z])YamlSerializer([^a-zA-Z])=\1ObjectMapYamlDeserializer\2"
    "com.mx.adapters.JsonObjectMapDeserializer([^a-zA-Z])=com.mx.common.serialization.ObjectMapJsonDeserializer\1"
    "([^a-zA-Z])JsonObjectMapDeserializer([^a-zA-Z])=\1ObjectMapJsonDeserializer\2"

    "([^a-zA-Z])com.mx.common.process.FaultTolerantExecutionFailureStatus([^a-zA-Z])=\1com.mx.common.accessors.PathResponseStatus\2"
    "([^a-zA-Z])AccessorResponseStatus([^a-zA-Z])=\1PathResponseStatus\2"
    "([^a-zA-Z])AccessorResponseStatus.GATEWAY_TIMEOUT([^a-zA-Z])=\1PathResponseStatus.TIMEOUT\2"
    "([^a-zA-Z])AccessorResponseStatus.SERVICE_UNAVAILABLE([^a-zA-Z])=\1PathResponseStatus.UNAVAILABLE\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_TIMEOUT([^a-zA-Z])=\1PathResponseStatus.TIMEOUT\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_EXECUTION_UNAVAILABLE([^a-zA-Z])=\1PathResponseStatus.UNAVAILABLE\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.TASK_LIMIT_EXCEEDED([^a-zA-Z])=\1PathResponseStatus.TOO_MANY_REQUESTS\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus.INTERNAL_ERROR([^a-zA-Z])=\1PathResponseStatus.INTERNAL_ERROR\2"
    "([^a-zA-Z])FaultTolerantExecutionFailureStatus([^a-zA-Z])=\1PathResponseStatus\2"

    "com.mx.path.gateway.GatewayException([^a-zA-Z])=com.mx.common.gateway.GatewayException\1"
    "com.mx.common.accessors.AccessorException([^a-zA-Z])=com.mx.common.exception.AccessorException\1"
    "com.mx.common.accessors.ConnectException([^a-zA-Z])=com.mx.common.exception.ConnectException\1"

    "AccessorException\(PathResponseStatus.NOT_IMPLEMENTED\)=AccessorMethodNotImplementedException\(\)"
    "package com.mx.accessors([_a-zA-Z\.]*)([^_a-zA-Z\.])$=package com.mx.accessors\1\2\nimport com.mx.common.exception.AccessorMethodNotImplementedException\nimport com.mx.common.gateway.GatewayAPI\nimport com.mx.common.accessors.API\nimport com.mx.common.accessors.AccessorConfiguration\nimport com.mx.common.accessors.Accessor\nimport com.mx.common.accessors.RootAccessor\nimport com.mx.common.accessors.AccessorResponse\nimport com.mx.common.accessors.PathResponseStatus"
    "package com.mx.models([_a-zA-Z\.]*)([^_a-zA-Z\.])$=package com.mx.models\1\2\nimport com.mx.common.models.MdxBase\nimport com.mx.common.models.MdxWrappable\nimport com.mx.common.models.MdxList\nimport com.mx.common.models.Internal"
  )
  local gradle_mappings=(
    "JUNK=JUNK"
    #"([\"\'])com\.mx\.web:mdx-web:[^\"\']+([\"\'])=\1com.mx.web:mdx-web:$web_version\2"
  )

  local reported_file=false
  local mappings=("${global_mappings[@]}")

  if [ "${file: -5}" == ".java" ]; then
    mappings=("${mappings[@]}" "${java_mappings[@]}")
  elif [ "${file: -7}" == ".groovy" ] || [ "${file: -3}" == ".kt" ]; then
    mappings=("${mappings[@]}" "${groovy_mappings[@]}")
  elif [ "${file: -12}" == "build.gradle" ]; then
    mappings=("${mappings[@]}" "${gradle_mappings[@]}")
  else
    echo "Skipping non-code file $file"
  fi

  for pair in "${mappings[@]}"; do
    local from="${pair%%=*}"
    local to="${pair#*=}"

    changes="$(sed <"$file" -E -n "s/$from/$to/gp")"
    if [ -n "$changes" ]; then
      if ! $reported_file; then
        echo
        echo "Found match(es) in file: $file"
        reported_file=true
      fi
      echo "Changing \"$from\" to \"$to\", resulting in:"
      echo "$changes"
      if ! $DRY_RUN; then
        sed -E -i "" "s/$from/$to/g" "$file"
      fi
    fi
  done
  if $reported_file; then
    echo
    echo
  fi
}

### Driver
export -f process_file
export DRY_RUN
find "$ROOT_DIR" -type f \( -iname "*.java" -or -iname "*.groovy" -or -iname "build.gradle" \) -exec bash -c 'process_file "$0"' {} \;
exit 0
