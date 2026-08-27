import IosApp
import SwiftUI

@main
struct iOSApp: App {
    init() {
        URLCache.shared = URLCache(
            memoryCapacity: 50_000_000,
            diskCapacity: 100_000_000,
            diskPath: "kmpnews-image-cache"
        )
        KoinInit_iosKt.doInitAppKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
