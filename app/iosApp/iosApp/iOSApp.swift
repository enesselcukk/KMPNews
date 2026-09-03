import SwiftUI

@main
struct iOSApp: App {
    init() {
        KoinBootstrap.start()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}