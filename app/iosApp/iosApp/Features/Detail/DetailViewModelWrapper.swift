import IosApp
import SwiftUI

@MainActor
final class DetailViewModelWrapper: ObservableObject {
    @Published private(set) var snapshot: DetailUiStateSnapshot

    private let controller: DetailViewModelController

    init(articleUrl: String) {
        controller = DetailViewModelController(articleUrl: articleUrl)
        snapshot = controller.currentSnapshot()
        controller.observe { [weak self] newSnapshot in
            Task { @MainActor in
                self?.snapshot = newSnapshot
            }
        }
    }

    deinit {
        controller.close()
    }

    func goBack() {
        controller.onBackClicked()
    }

    func retry() {
        controller.retry()
    }

    func formattedPublishedDate() -> String {
        DetailFormattingIosKt.formatPublishedDate(isoDate: snapshot.publishedAt)
    }
}
