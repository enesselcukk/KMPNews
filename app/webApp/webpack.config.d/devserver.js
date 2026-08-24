;(function (config) {
    config.devServer = config.devServer || {};
    config.devServer.headers = [
        { key: 'Cross-Origin-Opener-Policy', value: 'same-origin' },
        { key: 'Cross-Origin-Embedder-Policy', value: 'require-corp' },
    ];
    config.devServer.proxy = [
        {
            context: ['/news-api'],
            target: 'https://newsapi.org',
            changeOrigin: true,
            secure: true,
            pathRewrite: { '^/news-api': '' },
        },
    ];
})(config);
