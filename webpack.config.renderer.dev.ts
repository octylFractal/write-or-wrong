import * as path from "path";
import * as webpack from "webpack";

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
    module: {
        rules: [
            {
                test: /\.ts(x)?$/,
                loader: "ts-loader",
                exclude: /node_modules/,
            }
        ]
    },
    resolve: {
        extensions: [".tsx", ".ts", ".js"]
    }
};

export default config;