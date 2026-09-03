import IosApp
import SwiftUI

struct HomeView: View {
    @ObservedObject var viewModel: HomeViewModelWrapper

    private var hasContent: Bool {
        !viewModel.snapshot.headlines.isEmpty || !viewModel.snapshot.feed.isEmpty
    }

    var body: some View {
        VStack(spacing: 0) {
            HomeTopBar(onSearch: viewModel.openSearch)

            ZStack(alignment: .top) {
                if viewModel.snapshot.isError && !hasContent {
                    HomeMessageView(
                        text: viewModel.snapshot.errorMessage ?? "An error occurred.",
                        actionTitle: "TRY AGAIN",
                        onAction: viewModel.retry,
                    )
                } else {
                    HomeContentView(
                        viewModel: viewModel,
                        showsInitialPlaceholder: viewModel.snapshot.isLoading && !hasContent,
                    )

                    if viewModel.snapshot.isLoading && !hasContent {
                        ProgressView()
                            .tint(KMPNewsTheme.orange)
                            .frame(maxWidth: .infinity, maxHeight: .infinity)
                            .background(KMPNewsTheme.background.opacity(0.6))
                    }
                }

                if viewModel.snapshot.isRefreshing && hasContent {
                    ProgressView()
                        .progressViewStyle(.linear)
                        .tint(KMPNewsTheme.orange)
                }
            }
        }
        .background(KMPNewsTheme.background)
    }
}

private struct HomeTopBar: View {
    let onSearch: () -> Void

    var body: some View {
        HStack(spacing: 12) {
            RoundedRectangle(cornerRadius: 10)
                .fill(KMPNewsTheme.orange.opacity(0.18))
                .frame(width: 44, height: 44)
                .overlay {
                    Text("SDH")
                        .font(.system(size: 16, weight: .bold))
                        .foregroundStyle(KMPNewsTheme.orange)
                }

            HStack(spacing: 6) {
                Text("SDH")
                    .font(.system(size: 22, weight: .bold))
                    .foregroundStyle(.white)
                Text("NEWS")
                    .font(.system(size: 22, weight: .bold))
                    .foregroundStyle(.white)
            }

            Spacer()

            Button(action: onSearch) {
                Image(systemName: "magnifyingglass")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(.white)
            }
            .buttonStyle(.plain)
        }
        .padding(.horizontal, 16)
        .padding(.vertical, 14)
        .background(KMPNewsTheme.navy)
    }
}

private struct HomeContentView: View {
    @ObservedObject var viewModel: HomeViewModelWrapper
    let showsInitialPlaceholder: Bool

    var body: some View {
        let headlines = viewModel.snapshot.headlines
        let feed = viewModel.snapshot.feed
        let hero = headlines.first ?? feed.first

        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                if showsInitialPlaceholder {
                    HomeHeroPlaceholder()
                } else if let hero {
                    HomeHeroView(
                        article: hero,
                        relativeTime: viewModel.relativeTime(for: hero.publishedAt),
                        readTime: viewModel.readTime(title: hero.title, description: hero.articleDescription),
                        onTap: { viewModel.openDetail(hero.url) },
                    )
                }

                HomeCategoryTabs(
                    categories: viewModel.snapshot.categories,
                    selectedCategory: viewModel.snapshot.selectedCategory ?? "general",
                    label: viewModel.categoryLabel(for:),
                    onSelect: viewModel.selectCategory,
                )

                Text("FEATURED NEWS")
                    .font(.system(size: 14, weight: .bold))
                    .kerning(1)
                    .foregroundStyle(KMPNewsTheme.onBackground)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 12)

                if showsInitialPlaceholder {
                    ForEach(0..<4, id: \.self) { _ in
                        FeaturedNewsCardPlaceholder()
                            .padding(.horizontal, 16)
                            .padding(.vertical, 6)
                    }
                } else {
                    let featured = feed.filter { $0.url != hero?.url }
                    ForEach(featured, id: \.url) { article in
                        FeaturedNewsCard(
                            article: article,
                            relativeTime: viewModel.relativeTime(for: article.publishedAt),
                            onTap: { viewModel.openDetail(article.url) },
                        )
                        .padding(.horizontal, 16)
                        .padding(.vertical, 6)
                    }
                }
            }
            .padding(.bottom, 24)
        }
        .scrollDisabled(showsInitialPlaceholder)
    }
}

private struct HomeHeroPlaceholder: View {
    var body: some View {
        Color.clear
            .aspectRatio(4 / 3, contentMode: .fit)
            .overlay {
                ImagePlaceholderView(title: nil)
            }
            .clipped()
    }
}

private struct FeaturedNewsCardPlaceholder: View {
    var body: some View {
        HStack(spacing: 12) {
            RoundedRectangle(cornerRadius: 12)
                .fill(KMPNewsTheme.outlineVariant.opacity(0.35))
                .frame(width: 72, height: 72)

            VStack(alignment: .leading, spacing: 8) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(KMPNewsTheme.outlineVariant.opacity(0.35))
                    .frame(height: 14)
                RoundedRectangle(cornerRadius: 4)
                    .fill(KMPNewsTheme.outlineVariant.opacity(0.25))
                    .frame(width: 80, height: 12)
            }

            Spacer(minLength: 0)
        }
        .padding(14)
        .background(
            RoundedRectangle(cornerRadius: 14)
                .stroke(KMPNewsTheme.outlineVariant, lineWidth: 1)
                .background(
                    RoundedRectangle(cornerRadius: 14)
                        .fill(KMPNewsTheme.surface),
                ),
        )
    }
}

private struct HomeCategoryTabs: View {
    let categories: [String]
    let selectedCategory: String
    let label: (String) -> String
    let onSelect: (String) -> Void

    var body: some View {
        ScrollView(.horizontal, showsIndicators: false) {
            HStack(spacing: 20) {
                ForEach(categories, id: \.self) { category in
                    let selected = category == selectedCategory
                    Button {
                        onSelect(category)
                    } label: {
                        VStack(spacing: 6) {
                            Text(label(category))
                                .font(.system(size: 12, weight: selected ? .bold : .medium))
                                .kerning(0.6)
                                .foregroundStyle(selected ? KMPNewsTheme.orange : KMPNewsTheme.onSurfaceVariant)

                            Capsule()
                                .fill(selected ? KMPNewsTheme.orange : .clear)
                                .frame(width: selected ? 24 : 0, height: 2)
                        }
                    }
                    .buttonStyle(.plain)
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 14)
        }
    }
}

private struct HomeHeroView: View {
    let article: HomeArticleSnapshot
    let relativeTime: String
    let readTime: String
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            Color.clear
                .aspectRatio(4 / 3, contentMode: .fit)
                .overlay {
                    RemoteImageView(urlString: article.imageUrl, title: article.title)
                }
                .overlay {
                    LinearGradient(
                        colors: [.clear, .black.opacity(0.92)],
                        startPoint: .center,
                        endPoint: .bottom,
                    )
                }
                .overlay(alignment: .bottomLeading) {
                    VStack(alignment: .leading, spacing: 10) {
                        Text(article.title.uppercased())
                            .font(.system(size: 22, weight: .bold))
                            .foregroundStyle(.white)
                            .multilineTextAlignment(.leading)
                            .lineLimit(3)

                        HStack(spacing: 16) {
                            Text(relativeTime)
                            Text(readTime)
                        }
                        .font(.system(size: 12, weight: .medium))
                        .foregroundStyle(KMPNewsTheme.orange)
                    }
                    .padding(20)
                }
                .clipped()
        }
        .buttonStyle(.plain)
    }
}

private struct FeaturedNewsCard: View {
    let article: HomeArticleSnapshot
    let relativeTime: String
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                RemoteImageView(urlString: article.imageUrl, title: article.title)
                    .frame(width: 72, height: 72)
                    .clipShape(RoundedRectangle(cornerRadius: 12))

                VStack(alignment: .leading, spacing: 6) {
                    Text(article.title.uppercased())
                        .font(.system(size: 13, weight: .bold))
                        .foregroundStyle(KMPNewsTheme.onBackground)
                        .multilineTextAlignment(.leading)
                        .lineLimit(2)

                    Text(relativeTime)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundStyle(KMPNewsTheme.orange)
                }
            }
            .padding(14)
            .background(
                RoundedRectangle(cornerRadius: 14)
                    .stroke(KMPNewsTheme.outlineVariant, lineWidth: 1)
                    .background(
                        RoundedRectangle(cornerRadius: 14)
                            .fill(KMPNewsTheme.surface),
                    ),
            )
        }
        .buttonStyle(.plain)
    }
}

private struct HomeMessageView: View {
    let text: String
    let actionTitle: String
    let onAction: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            Text(text)
                .multilineTextAlignment(.center)
                .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
            Button(actionTitle, action: onAction)
                .font(.system(size: 14, weight: .bold))
                .foregroundStyle(KMPNewsTheme.orange)
            Spacer()
        }
        .padding(24)
    }
}
