import { FocusEvent, KeyboardEvent, MouseEvent, useState } from "react";

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

    function handleKeyDown(e: KeyboardEvent) {
        e.preventDefault();
        console.log("Key down");
        setEditorState((prev) => ({
            blocks: [{text: prev.blocks[0]!.text + e.key}]
        }));
    }

    function handleFocus(e: FocusEvent) {
        console.log("Focused");
    }

    function handleClick(e: MouseEvent) {
        console.log("Clicked");
        e.stopPropagation();
        e.preventDefault();
        (e.currentTarget as any).focus();
    }

    return <div id="editor">
        <p className="editor-block" onClick={handleClick} onFocus={handleFocus} onKeyDown={handleKeyDown} tabIndex={-1}>{editorState.blocks[0]!.text}</p>
    </div>
}