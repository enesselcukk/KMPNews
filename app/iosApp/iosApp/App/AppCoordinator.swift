import IosApp
import SwiftUI

@MainActor
final class AppCoordinator: ObservableObject {
    @Published var path = NavigationPath()

    private var navigationBridge: IosNavigationBridge?

    func start() {
        guard navigationBridge == nil else { return }

        let bridge = IosNavigationBridge()
        bridge.onNavigateToDetail = { [weak self] url in
            Task { @MainActor in
                self?.path.append(AppRoute.detail(url))
            }
        }
        bridge.onNavigateToSearch = { [weak self] in
            Task { @MainActor in
                self?.path.append(AppRoute.search)
            }
        }
        bridge.onNavigateUp = { [weak self] in
            Task { @MainActor in
                guard let self, !self.path.isEmpty else { return }
                self.path.removeLast()
            }
        }
        bridge.start()
        navigationBridge = bridge
    }
}
