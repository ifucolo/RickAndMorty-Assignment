package com.ifucolo.rickandmorty

import androidx.activity.ComponentActivity
import dagger.hilt.android.AndroidEntryPoint

/**
 * This needed to use hiltViewModel() in tests, because only component activity with android rule does not work
 * */
@AndroidEntryPoint
class HiltComponentActivity: ComponentActivity()