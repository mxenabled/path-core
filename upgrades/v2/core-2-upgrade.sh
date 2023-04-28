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
  #local sdk_version="18.+"
  #local facilities_version="10.+"
  #local web_version="32.+"

  export nl=$'\n'
  local file=$1
  local java_mappings=(
    "com.mx.common.models.MdxList([^a-zA-Z])=com.mx.path.model.mdx.model.MdxList\1"
    "com.mx.common.models.MdxBase([^a-zA-Z])=com.mx.path.model.mdx.model.MdxBase\1"
    "com.mx.common.models.MdxWrappable([^a-zA-Z])=com.mx.common.models.ModelWrappable\1"
    "com.mx.common.remote.MdxListOfJson([^a-zA-Z])=com.mx.common.models.ParameterizedTypeImpl\1"
    "com.mx.path.model.mdx.model.MdxWrappableSerializer([^a-zA-Z])=com.mx.path.model.mdx.model.ModelWrappableSerializer\1"
    "Session.ServiceIdentifier.Session([^a-zA-Z])=Scope.Session\1"

    # Move packages. DO NOT CHANGE ORDER!
    "com\.mx\.common\.=com.mx.path.core.common."
    "com\.mx\.path\.core\.common\.accessors\.=com.mx.path.core.common.accessor."
    "com\.mx\.path\.core\.common\.collections\.=com.mx.path.core.common.collection."
    "com\.mx\.path\.core\.common\.events\.=com.mx.path.core.common.event."
    "com\.mx\.path\.core\.common\.models\.=com.mx.path.core.common.model."
    "com\.mx\.path\.gateway\.connect\.filters\.=com.mx.path.gateway.connect.filter."
    "com\.mx\.path\.gateway\.events\.=com.mx.path.gateway.event."
    "com\.mx\.path\.api\.connect\.http\.=com.mx.path.connect.http."
    "com\.mx\.path\.api\.connect\.messaging\.=com.mx.path.connect.messaging."
    "com\.mx\.path\.model\.context\.=com.mx.path.core.context."
    "com\.mx\.path\.utilities\.=com.mx.path.core.utility."
    "com\.mx\.path\.core\.utility\.OAuth\.=com.mx.path.core.utility.oauth."
    "com\.mx\.path\.connect\.http\.certificates\.=com.mx.path.connect.http.certificate."

    "com\.mx\.path\.core\.common\.accessor\.AccessorConfiguration([^a-zA-Z])=com.mx.path.gateway.accessor.AccessorConfiguration\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorConnectionBase([^a-zA-Z])=com.mx.path.gateway.accessor.AccessorConnectionBase\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorConnections([^a-zA-Z])=com.mx.path.gateway.accessor.AccessorConnections\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorMethodDefinition([^a-zA-Z])=com.mx.path.gateway.accessor.AccessorMethodDefinition\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorResponse([^a-zA-Z])=com.mx.path.gateway.accessor.AccessorResponse\1"
    "com\.mx\.path\.core\.common\.accessor\.Accessor([^a-zA-Z])=com.mx.path.gateway.accessor.Accessor\1"

    #"([^a-zA-Z])com.mx.common.process.FaultTolerantExecutionFailureStatus([^a-zA-Z])=\1com.mx.common.accessors.PathResponseStatus\2"
  )
  local groovy_mappings=(
    "com.mx.common.models.MdxList([^a-zA-Z]?)=com.mx.path.model.mdx.model.MdxList\1"
    "com.mx.common.models.MdxBase([^a-zA-Z]?)=com.mx.path.model.mdx.model.MdxBase\1"
    "com.mx.common.models.MdxWrappable([^a-zA-Z]?)=com.mx.common.models.ModelWrappable\1"
    "com.mx.common.remote.MdxListOfJson([^a-zA-Z]?)=com.mx.common.models.ParameterizedTypeImpl\1"
    "com.mx.path.model.mdx.model.MdxWrappableSerializer([^a-zA-Z]?)=com.mx.path.model.mdx.model.ModelWrappableSerializer\1"
    "Session.ServiceIdentifier.Session([^a-zA-Z]?)=Scope.Session\1"

    # Move packages. DO NOT CHANGE ORDER!
    "com\.mx\.common\.=com.mx.path.core.common."
    "com\.mx\.path\.core\.common\.accessors\.=com.mx.path.core.common.accessor."
    "com\.mx\.path\.core\.common\.collections\.=com.mx.path.core.common.collection."
    "com\.mx\.path\.core\.common\.events\.=com.mx.path.core.common.event."
    "com\.mx\.path\.core\.common\.models\.=com.mx.path.core.common.model."
    "com\.mx\.path\.gateway\.connect\.filters\.=com.mx.path.gateway.connect.filter."
    "com\.mx\.path\.gateway\.events\.=com.mx.path.gateway.event."
    "com\.mx\.path\.api\.connect\.http\.=com.mx.path.connect.http."
    "com\.mx\.path\.api\.connect\.messaging\.=com.mx.path.connect.messaging."
    "com\.mx\.path\.model\.context\.=com.mx.path.core.context."
    "com\.mx\.path\.utilities\.=com.mx.path.core.utility."
    "com\.mx\.path\.core\.utility\.OAuth\.=com.mx.path.core.utility.oauth."
    "com\.mx\.path\.connect\.http\.certificates\.=com.mx.path.connect.http.certificate."

    "com\.mx\.path\.core\.common\.accessor\.AccessorConfiguration([^a-zA-Z]?)=com.mx.path.gateway.accessor.AccessorConfiguration\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorConnectionBase([^a-zA-Z]?)=com.mx.path.gateway.accessor.AccessorConnectionBase\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorConnections([^a-zA-Z]?)=com.mx.path.gateway.accessor.AccessorConnections\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorMethodDefinition([^a-zA-Z]?)=com.mx.path.gateway.accessor.AccessorMethodDefinition\1"
    "com\.mx\.path\.core\.common\.accessor\.AccessorResponse([^a-zA-Z]?)=com.mx.path.gateway.accessor.AccessorResponse\1"
    "com\.mx\.path\.core\.common\.accessor\.Accessor(;?)$=com.mx.path.gateway.accessor.Accessor\1"

    #"com.mx.serializers.YamlSerializer([^a-zA-Z]?)=com.mx.common.serialization.ObjectMapYamlDeserializer\1"
    #"([^a-zA-Z])YamlSerializer([^a-zA-Z]?)=\1ObjectMapYamlDeserializer\2"
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
