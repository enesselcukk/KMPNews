import IosApp

enum KoinBootstrap {
    static func start() {
        KoinInit_iosKt.doInitAppKoin()
    }
}
