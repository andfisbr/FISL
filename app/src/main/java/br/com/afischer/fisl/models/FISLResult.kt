package br.com.afischer.fisl.models

import br.com.afischer.fisl.enums.ResultType

class FISLResult(var type: Int = ResultType.ERROR, var message: String = "")
