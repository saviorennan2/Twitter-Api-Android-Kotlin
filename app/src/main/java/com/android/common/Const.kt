package com.android.common

//Base URL
const val BASE_URL = "https://api.twitter.com/"

//Endpoints
const val OAUTH2_TOKEN_SUFFIX = "oauth2/token"
const val SEARCH_SUFFIX = "1.1/search/tweets.json"

//Twitter Api
const val CONSUMER_API_KEY = "k718SW31ef44meXuyyRvWIebt"
const val CONSUMER_API_SECRET_KEY = "X0e8mdsAdUC3HVWza7mR4W76VT3RjjYQLg2RQHsGegnEDkBAjB"
const val BEARER_TOKEN_CREDENTIALS = "$CONSUMER_API_KEY:$CONSUMER_API_SECRET_KEY"
const val GRANT_TYPE = "client_credentials"

//Pref
const val PREF_TWITTER_TOKEN_TYPE = "token_type"
const val PREF_TWITTER_ACCESS_TOKEN = "access_token"