import SwiftUI
import UIKit

enum ImageLoaderCache {
    static let session: URLSession = {
        let cache = URLCache(memoryCapacity: 50_000_000, diskCapacity: 100_000_000)
        let config = URLSessionConfiguration.default
        config.urlCache = cache
        config.requestCachePolicy = .returnCacheDataElseLoad
        return URLSession(configuration: config)
    }()

    static let memory = NSCache<NSString, UIImage>()
}

@MainActor
final class RemoteImageLoader: ObservableObject {
    @Published private(set) var image: UIImage?
    @Published private(set) var didFail = false

    private let url: URL
    private var task: URLSessionDataTask?

    init(url: URL) {
        self.url = url
        if let cached = ImageLoaderCache.memory.object(forKey: url.absoluteString as NSString) {
            image = cached
        } else {
            load()
        }
    }

    deinit {
        task?.cancel()
    }

    private func load() {
        task = ImageLoaderCache.session.dataTask(with: url) { data, response, _ in
            guard
                let data,
                let response,
                let loaded = UIImage(data: data),
                response.isImageResponse
            else {
                Task { @MainActor in
                    self.didFail = true
                }
                return
            }

            ImageLoaderCache.memory.setObject(loaded, forKey: self.url.absoluteString as NSString)
            Task { @MainActor in
                self.image = loaded
            }
        }
        task?.resume()
    }
}

private extension URLResponse {
    var isImageResponse: Bool {
        (mimeType?.hasPrefix("image/") ?? false) || (url?.pathExtension.isEmpty == false)
    }
}

struct RemoteImageView: View {
    let urlString: String?
    let title: String?

    var body: some View {
        Group {
            if let urlString, let url = URL(string: urlString) {
                RemoteImageContent(url: url, title: title)
                    .id(url.absoluteString)
            } else {
                ImagePlaceholderView(title: title)
            }
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}

private struct RemoteImageContent: View {
    let url: URL
    let title: String?

    @StateObject private var loader: RemoteImageLoader

    init(url: URL, title: String?) {
        self.url = url
        self.title = title
        _loader = StateObject(wrappedValue: RemoteImageLoader(url: url))
    }

    var body: some View {
        Group {
            if let image = loader.image {
                Image(uiImage: image)
                    .resizable()
                    .scaledToFill()
            } else {
                ImagePlaceholderView(title: title)
            }
        }
        .frame(minWidth: 0, maxWidth: .infinity, minHeight: 0, maxHeight: .infinity)
        .clipped()
        .animation(nil, value: loader.image == nil)
    }
}

struct ImagePlaceholderView: View {
    let title: String?

    var body: some View {
        let seed = abs(title?.hashValue ?? 0)
        let colors: [Color] = [
            [KMPNewsTheme.orange.opacity(0.35), KMPNewsTheme.orange],
            [KMPNewsTheme.navy.opacity(0.35), KMPNewsTheme.navy],
            [Color.blue.opacity(0.25), Color.blue],
            [Color.gray.opacity(0.25), Color.gray],
        ][seed % 4]

        LinearGradient(colors: colors, startPoint: .topLeading, endPoint: .bottomTrailing)
            .frame(maxWidth: .infinity, maxHeight: .infinity)
    }
}
