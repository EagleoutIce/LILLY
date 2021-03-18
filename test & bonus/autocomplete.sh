#!/bin/bash

# will be built by CMD DESC CMD DESC .....
JAKE_OPERATIONS_ARRAY=(Hallo Welt "Wie:geht" es " dir?")

# bash only
# ## Searches if the search is prefix of an array-element
# ## To be NOT bash-dependent, sed will be used
# # $1 will be the target-array-name
# # $2 the prefix subsitution
# # $3 the search-word
# # $4 suffix sub
# # Returns: Prefix-sub if not found, suffix otherwise
# _jake_find_in_array_prefix() {
#     arr=$1[@]
#     for elem in "${JAKE_OPERATIONS_ARRAY[@]}"; do
#         echo "checking: $elem"
#         if [ "$(echo $elem|sed -e "s/$2//")" == "$3" ]; then # ${elem%$2}
#             echo "$(echo $elem|sed -e "s/$4//")" # ${elem#$4}
#             return
#         fi
#     done;
#     echo "$2"
# }


## Determines if $1 is a valid operation
# $1 String to check
# Returns: 1 if true, 0 otherwise
_jake_is_operation() {
    # ret=$(_jake_find_in_array_prefix "JAKE_OPERATIONS_ARRAY" ":.*" "$1" ".*:")
    for i in {0..${#JAKE_OPERATIONS_ARRAY[@]}}; do
        if [ "${JAKE_OPERATIONS_ARRAY[$i]}" == $1 ]; then
            echo 1
            return
        fi
    done;
    echo 0
}

## Will check if there has been a valid operation in the array 'COMP_WORDS'
# Returns: 1 if operation given, 0 otherwise
_jake_operation_given() {
    for word in "${COMP_WORDS[@]}"; do
        if [$(_jake_is_operation "$word") == 1]; then
            echo 1
            return
        fi
    done
    echo 0
}

# Main-Routine
_java_jake() {
    COMPREPLY=() # The Feedback we want to give, we will clear it to set it later
    cur="${COMP_WORDS[COMP_CWORD]}" # The Current-Word in the autocompletion.
    echo "$(_jake_operation_given)"
}

complete -o default -F _java_jake test.sh