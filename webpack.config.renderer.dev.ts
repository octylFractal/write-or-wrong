import * as path from "path";
import * as webpack from "webpack";
import MiniCssExtractPlugin from "mini-css-extract-plugin";

const config: webpack.Configuration = {
    mode: "development",
    devtool: "inline-source-map",
    target: ["web", "electron-renderer"],
    entry: [
        "./src/renderer/index.tsx"
    ],
    output: {
        path: path.resolve(__dirname, "dist"),
        filename: "renderer.bundle.js"
    },
    plugins: [new MiniCssExtractPlugin()],
    module: {
        rules: [
            {
                test: /\.ts(x)?$/,
                loader: "ts-loader",
                exclude: /node_modules/,
            },
            {
                test: /\.styl$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    "css-loader",
                    "stylus-loader"
                ]
            },

        ]
    },
    resolve: {
        extensions: [".tsx", ".ts", ".js"]
    }
};

export default config;