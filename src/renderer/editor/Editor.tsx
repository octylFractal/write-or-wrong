import { KeyboardEvent, useState } from "react";

interface EditorBlock {
    text: string;
}

interface EditorState {
    blocks: EditorBlock[];
}

export default function Editor() {
    const [editorState, setEditorState] = useState<EditorState>({blocks: [
        {text: ""}
    ]});

    function handleOnClick(e: KeyboardEvent) {
        e.preventDefault();
        setEditorState((prev) => ({
            blocks: [{text: prev.blocks[0]!.text + e.key}]
        }));
    }

    return <div id="editor">
        <p className="editor-block" onKeyDown={handleOnClick}>{editorState.blocks[0]!.text}</p>
    </div>
}