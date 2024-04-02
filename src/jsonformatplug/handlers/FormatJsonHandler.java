package jsonformatplug.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class FormatJsonHandler extends AbstractHandler {

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editorPart = HandlerUtil.getActiveEditor(event);
        IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
        if (editorPart != null) {
        	if (editorPart instanceof ITextEditor) {
                ITextEditor textEditor = (ITextEditor) editorPart;
                IDocument document = textEditor.getDocumentProvider().getDocument(editorPart.getEditorInput());
                ISelection selection = textEditor.getSelectionProvider().getSelection();
                if (selection instanceof ITextSelection) {
                    ITextSelection textSelection = (ITextSelection) selection;
                    int offset = textSelection.getOffset();
                    int length = textSelection.getLength();
                    try {
                        String selectedText = document.get(offset, length);
                        if (selectedText==null||"".equals(selectedText)) {
                        	MessageDialog.openInformation(
                    				window.getShell(),
                    				"JsonFormat",
                    				"请选择要格式化的内容");
                        	return null;
						}
                        JSONObject jsonObject = JSONObject.parseObject(selectedText.replaceAll("\\\\\"", "\""));
                        String formattedJson = JSONObject.toJSONString(jsonObject,SerializerFeature.PrettyFormat);
                        document.replace(offset, length, formattedJson);
                    } catch (JSONException e) {
                    	MessageDialog.openInformation(
                				window.getShell(),
                				"JsonFormat",
                				e.getMessage());
					} catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

}
