import IosApp
import SwiftUI

struct ContentView: View {
    @StateObject private var coordinator = AppCoordinator()
    @StateObject private var homeViewModel = HomeViewModelWrapper()

    var body: some View {
        NavigationStack(path: $coordinator.path) {
            HomeView(viewModel: homeViewModel)
                .navigationDestination(for: AppRoute.self) { route in
                    switch route {
                    case .search:
                        SearchView()
                    case .detail(let articleUrl):
                        DetailView(articleUrl: articleUrl)
                    }
                }
        }
        .onAppear {
            coordinator.start()
        }
    }
}

#Preview {
    let _ = KoinBootstrap.start()
    ContentView()
}
