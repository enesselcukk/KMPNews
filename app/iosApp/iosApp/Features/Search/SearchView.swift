import IosApp
import SwiftUI

struct SearchView: View {
    @StateObject private var viewModel = SearchViewModelWrapper()
    @FocusState private var isSearchFocused: Bool
    @State private var query = ""

    var body: some View {
        VStack(spacing: 0) {
            SearchTopBar(
                query: $query,
                isSearchFocused: $isSearchFocused,
                onQueryChange: { newValue in
                    query = newValue
                    viewModel.updateQuery(newValue)
                },
                onSubmit: viewModel.submit,
                onClear: {
                    query = ""
                    viewModel.clearQuery()
                },
                onBack: viewModel.goBack,
            )

            if viewModel.snapshot.isLoading {
                ProgressView()
                    .progressViewStyle(.linear)
                    .tint(KMPNewsTheme.orange)
            }

            if viewModel.snapshot.showIdlePrompt {
                SearchMessageView(text: "Search for topics, people, or headlines")
            } else if viewModel.snapshot.isQueryTooShort {
                SearchMessageView(text: "Enter at least 2 characters")
            } else if let errorMessage = viewModel.snapshot.errorMessage,
                      viewModel.snapshot.results.isEmpty {
                SearchErrorView(message: errorMessage, onRetry: viewModel.retry)
            } else if viewModel.snapshot.showEmptyResults {
                SearchMessageView(text: "No articles found for \"\(viewModel.snapshot.query.trimmingCharacters(in: .whitespacesAndNewlines))\"")
            } else if !viewModel.snapshot.results.isEmpty {
                ScrollView {
                    LazyVStack(spacing: 12) {
                        ForEach(viewModel.snapshot.results, id: \.url) { result in
                            SearchResultRow(
                                result: result,
                                relativeTime: viewModel.relativeTime(for: result.publishedAt),
                                onTap: { viewModel.openResult(result.url) },
                            )
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.vertical, 12)
                }
            } else if viewModel.snapshot.isLoading {
                Spacer()
                ProgressView()
                    .tint(KMPNewsTheme.orange)
                Spacer()
            }
        }
        .background(KMPNewsTheme.background)
        .navigationBarBackButtonHidden(true)
        .onAppear {
            isSearchFocused = true
        }
        .onChange(of: viewModel.snapshot.query) { _, newValue in
            if query != newValue {
                query = newValue
            }
        }
    }
}

private struct SearchTopBar: View {
    @Binding var query: String
    @FocusState.Binding var isSearchFocused: Bool
    let onQueryChange: (String) -> Void
    let onSubmit: () -> Void
    let onClear: () -> Void
    let onBack: () -> Void

    var body: some View {
        HStack(spacing: 8) {
            Button(action: onBack) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 17, weight: .semibold))
                    .foregroundStyle(.white)
                    .frame(width: 44, height: 44)
            }

            HStack(spacing: 8) {
                Image(systemName: "magnifyingglass")
                    .foregroundStyle(KMPNewsTheme.onSurfaceVariant)

                TextField("Search news articles", text: $query)
                    .focused($isSearchFocused)
                    .textInputAutocapitalization(.never)
                    .autocorrectionDisabled()
                    .submitLabel(.search)
                    .onSubmit(onSubmit)
                    .onChange(of: query) { _, newValue in
                        onQueryChange(newValue)
                    }

                if !query.isEmpty {
                    Button(action: onClear) {
                        Image(systemName: "xmark.circle.fill")
                            .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
                    }
                }
            }
            .padding(.horizontal, 12)
            .padding(.vertical, 10)
            .background(
                RoundedRectangle(cornerRadius: 12)
                    .fill(.white),
            )
        }
        .padding(.horizontal, 8)
        .padding(.vertical, 8)
        .background(KMPNewsTheme.navy)
    }
}

private struct SearchResultRow: View {
    let result: SearchResultSnapshot
    let relativeTime: String
    let onTap: () -> Void

    var body: some View {
        Button(action: onTap) {
            HStack(spacing: 12) {
                RemoteImageView(urlString: result.imageUrl, title: result.title)
                    .frame(width: 72, height: 72)
                    .clipShape(RoundedRectangle(cornerRadius: 12))

                VStack(alignment: .leading, spacing: 6) {
                    Text(result.sourceName.uppercased())
                        .font(.system(size: 11, weight: .bold))
                        .foregroundStyle(KMPNewsTheme.orange)
                        .lineLimit(1)

                    Text(result.title.uppercased())
                        .font(.system(size: 13, weight: .bold))
                        .foregroundStyle(KMPNewsTheme.onBackground)
                        .multilineTextAlignment(.leading)
                        .lineLimit(2)

                    Text(relativeTime)
                        .font(.system(size: 12, weight: .medium))
                        .foregroundStyle(KMPNewsTheme.orange)
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
        .buttonStyle(.plain)
    }
}

private struct SearchMessageView: View {
    let text: String

    var body: some View {
        VStack {
            Spacer()
            Text(text)
                .multilineTextAlignment(.center)
                .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
                .padding(24)
            Spacer()
        }
    }
}

private struct SearchErrorView: View {
    let message: String
    let onRetry: () -> Void

    var body: some View {
        VStack(spacing: 16) {
            Spacer()
            Text(message)
                .multilineTextAlignment(.center)
                .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
            Button("TRY AGAIN", action: onRetry)
                .font(.system(size: 14, weight: .bold))
                .foregroundStyle(KMPNewsTheme.orange)
            Spacer()
        }
        .padding(24)
    }
}
