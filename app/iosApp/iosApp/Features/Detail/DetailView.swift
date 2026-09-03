import SwiftUI

struct DetailView: View {
    @StateObject private var viewModel: DetailViewModelWrapper

    init(articleUrl: String) {
        _viewModel = StateObject(wrappedValue: DetailViewModelWrapper(articleUrl: articleUrl))
    }

    var body: some View {
        VStack(spacing: 0) {
            DetailTopBar(onBack: viewModel.goBack)

            if viewModel.snapshot.isLoading {
                Spacer()
                ProgressView()
                    .tint(KMPNewsTheme.orange)
                Spacer()
            } else if viewModel.snapshot.errorMessage != nil && viewModel.snapshot.title.isEmpty {
                DetailErrorView(
                    message: viewModel.snapshot.errorMessage ?? "Unable to load this article.",
                    onRetry: viewModel.retry,
                    onBack: viewModel.goBack,
                )
            } else {
                DetailContentView(viewModel: viewModel)
            }
        }
        .background(KMPNewsTheme.background)
        .navigationBarBackButtonHidden(true)
    }
}

private struct DetailTopBar: View {
    let onBack: () -> Void

    var body: some View {
        HStack {
            Button(action: onBack) {
                Image(systemName: "chevron.left")
                    .font(.system(size: 17, weight: .semibold))
                    .foregroundStyle(.white)
                    .frame(width: 44, height: 44)
            }
            Spacer()
        }
        .frame(height: 48)
        .background(KMPNewsTheme.navy)
    }
}

private struct DetailContentView: View {
    @ObservedObject var viewModel: DetailViewModelWrapper

    var body: some View {
        ScrollView {
            VStack(alignment: .leading, spacing: 0) {
                DetailHeroImage(
                    imageUrl: viewModel.snapshot.imageUrl,
                    sourceName: viewModel.snapshot.sourceName,
                )

                VStack(alignment: .leading, spacing: 12) {
                    HStack(spacing: 8) {
                        Text(viewModel.snapshot.sourceName.uppercased())
                            .font(.system(size: 12, weight: .bold))
                            .foregroundStyle(.white)
                            .padding(.horizontal, 10)
                            .padding(.vertical, 4)
                            .background(Capsule().fill(KMPNewsTheme.orange))

                        Text(viewModel.formattedPublishedDate())
                            .font(.system(size: 12, weight: .medium))
                            .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
                    }

                    Text(viewModel.snapshot.title)
                        .font(.system(size: 24, weight: .bold))
                        .foregroundStyle(KMPNewsTheme.onBackground)

                    if let author = viewModel.snapshot.author, !author.isEmpty {
                        Text("By \(author)")
                            .font(.system(size: 12, weight: .medium))
                            .foregroundStyle(KMPNewsTheme.onSurfaceVariant)
                    }

                    if let description = viewModel.snapshot.articleDescription, !description.isEmpty {
                        Text(description)
                            .font(.system(size: 16, weight: .medium))
                            .foregroundStyle(KMPNewsTheme.onBackground)
                            .lineSpacing(4)
                    }

                    if viewModel.snapshot.isLoading {
                        ProgressView()
                            .tint(KMPNewsTheme.orange)
                            .frame(maxWidth: .infinity)
                            .padding(.vertical, 8)
                    }
                }
                .padding(.horizontal, 20)
                .padding(.vertical, 20)

                if let content = viewModel.snapshot.content, !content.isEmpty {
                    Divider()
                        .padding(.horizontal, 20)

                    VStack(alignment: .leading, spacing: 12) {
                        Text("Continue reading")
                            .font(.system(size: 12, weight: .bold))
                            .kerning(0.8)
                            .foregroundStyle(KMPNewsTheme.orange)

                        Text(content)
                            .font(.system(size: 16))
                            .foregroundStyle(KMPNewsTheme.onBackground)
                            .lineSpacing(6)
                    }
                    .padding(.horizontal, 20)
                    .padding(.vertical, 16)
                }

                Spacer(minLength: 24)
            }
        }
    }
}

private struct DetailHeroImage: View {
    let imageUrl: String?
    let sourceName: String

    var body: some View {
        ZStack(alignment: .bottomLeading) {
            RemoteImageView(urlString: imageUrl, title: sourceName)

            LinearGradient(
                colors: [.clear, .black.opacity(0.72)],
                startPoint: .center,
                endPoint: .bottom,
            )

            Text(sourceName.uppercased())
                .font(.system(size: 12, weight: .bold))
                .kerning(1)
                .foregroundStyle(.white)
                .padding(16)
        }
        .frame(maxWidth: .infinity)
        .aspectRatio(16 / 9, contentMode: .fit)
        .clipped()
    }
}

private struct DetailErrorView: View {
    let message: String
    let onRetry: () -> Void
    let onBack: () -> Void

    var body: some View {
        VStack(spacing: 20) {
            Spacer()
            Text(message)
                .multilineTextAlignment(.center)
                .foregroundStyle(KMPNewsTheme.onSurfaceVariant)

            Button("TRY AGAIN", action: onRetry)
                .font(.system(size: 14, weight: .bold))
                .foregroundStyle(KMPNewsTheme.orange)

            Button("Go back", action: onBack)
                .font(.system(size: 14, weight: .medium))
                .foregroundStyle(KMPNewsTheme.navy)

            Spacer()
        }
        .padding(24)
    }
}
