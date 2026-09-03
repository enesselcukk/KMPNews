import IosApp
import SwiftUI

@MainActor
final class SearchViewModelWrapper: ObservableObject {
    @Published private(set) var snapshot: SearchUiStateSnapshot

    private let controller = SearchViewModelController()

    init() {
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

    func updateQuery(_ query: String) {
        controller.onQueryChange(query: query)
    }

    func submit() {
        controller.onSubmit()
    }

    func clearQuery() {
        controller.onClearQuery()
    }

    func openResult(_ url: String) {
        controller.onResultSelected(articleUrl: url)
    }

    func goBack() {
        controller.onBack()
    }

    func retry() {
        controller.retry()
    }

    func relativeTime(for publishedAt: String?) -> String {
        HomeFormattingIosKt.formatHomeRelativeTime(publishedAt: publishedAt)
    }
}
