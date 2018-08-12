# Features of jWebform

List of features:

## Core

* central form-definition for reuse and separation of concerns
* avoiding a lot of boilerplate code
* very compact output in sourcecode of template

## Details

* handling the request-response loop for forms
* handling error-messages and error-highlighting of the labels
* correct tab-order
* pre-fillments of form-elements
* automatically syntactical correct HTML (5) with label and "label-for"
* pre-fillments of user-input after submit
* max-lenght constraint on every element to avoid security issues (TBD)
* easy prebuild validation variants, easy custom validation
* all standard form fields and more (date-input)
* prepared for multilang-setups
* warn, if identical names are choosen
* automatically set correct transfer mode if fileupload elment is used

## Available extensions

* prebuild Bootstrap Styles via theme project
* prebuild ajax handling via integration project

# Architecture

* no dependencies
* mostly no nulls
* mostly immutable objects
* functional interfaces, lambdas
* clean code