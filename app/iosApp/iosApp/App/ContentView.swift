import IosApp
import SwiftUI

struct ContentView: View {
    @StateObject private var coordinator = AppCoordinator()
    @StateObject private var homeViewModel = HomeViewModelWrapper()

    var body: some View {
        NavigationStack(path: $coordinator.path) {
            HomeView(viewModel: homeViewModel)
                .navigationDestination(for: String.self) { articleUrl in
                    DetailView(articleUrl: articleUrl)
                }
        }
        .onAppear {
            coordinator.start()
        }
    }
}

#Preview {
    let _ = KoinInit_iosKt.doInitAppKoin()
    ContentView()
}
