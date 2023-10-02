package com.debduttapanda.j3.jerokit

enum class PageState {
    Balance,
    Peoples,
    Categories,
    GetAmount,
    PayAmount,
    Groups,
    Recents,
    Items,
    SplitCalculationWillGet,
    SplitCalculationWillPay,
}
enum class PageStateValue {
    Loading,
    Success,
    Error
}