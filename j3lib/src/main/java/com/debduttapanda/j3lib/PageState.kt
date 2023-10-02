package com.debduttapanda.j3lib

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