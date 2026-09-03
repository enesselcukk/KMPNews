import IosApp
import SwiftUI

@MainActor
final class HomeViewModelWrapper: ObservableObject {
    @Published private(set) var snapshot: HomeUiStateSnapshot

    private let controller: HomeViewModelController

    init() {
        KoinBootstrap.start()
        controller = HomeViewModelController()
        snapshot = controller.currentSnapshot()
        controller.observe { [weak self] newSnapshot in
            Task { @MainActor in
                guard let self, Self.shouldPublish(old: self.snapshot, new: newSnapshot) else { return }
                self.snapshot = newSnapshot
            }
        }
    }

    private static func shouldPublish(old: HomeUiStateSnapshot, new: HomeUiStateSnapshot) -> Bool {
        old.isLoading != new.isLoading ||
            old.isError != new.isError ||
            old.isRefreshing != new.isRefreshing ||
            old.selectedCategory != new.selectedCategory ||
            old.headlines.count != new.headlines.count ||
            old.feed.count != new.feed.count ||
            old.errorMessage != new.errorMessage ||
            old.headlines.first?.url != new.headlines.first?.url
    }

    deinit {
        controller.close()
    }

    func selectCategory(_ category: String) {
        controller.onCategorySelected(category: category)
    }

    func openDetail(_ url: String) {
        controller.navigateToDetail(newsId: url)
    }

    func retry() {
        controller.retry()
    }

    func categoryLabel(for categoryId: String) -> String {
        HomeFormattingIosKt.homeCategoryLabel(categoryId: categoryId)
    }

    func relativeTime(for publishedAt: String?) -> String {
        HomeFormattingIosKt.formatHomeRelativeTime(publishedAt: publishedAt)
    }

    func readTime(title: String, description: String?) -> String {
        HomeFormattingIosKt.formatHomeReadTime(title: title, description: description)
    }
}
