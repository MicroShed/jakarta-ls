package io.microshed.jakartals;

import org.eclipse.lsp4j.DidChangeTextDocumentParams;
import org.eclipse.lsp4j.DidCloseTextDocumentParams;
import org.eclipse.lsp4j.DidOpenTextDocumentParams;
import org.eclipse.lsp4j.DidSaveTextDocumentParams;
import org.eclipse.lsp4j.services.TextDocumentService;
import org.eclipse.lsp4mp.commons.DocumentFormat;
import org.eclipse.lsp4mp.ls.commons.TextDocument;
import org.eclipse.lsp4mp.ls.commons.TextDocuments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.eclipse.lsp4j.CodeAction;
import org.eclipse.lsp4j.CodeActionParams;
import org.eclipse.lsp4j.CodeLens;
import org.eclipse.lsp4j.CodeLensParams;
import org.eclipse.lsp4j.Command;
import org.eclipse.lsp4j.CompletionItem;
import org.eclipse.lsp4j.CompletionList;
import org.eclipse.lsp4j.CompletionParams;
import org.eclipse.lsp4j.DefinitionParams;
import org.eclipse.lsp4j.Diagnostic;
import org.eclipse.lsp4j.DocumentFormattingParams;
import org.eclipse.lsp4j.DocumentHighlight;
import org.eclipse.lsp4j.DocumentHighlightParams;
import org.eclipse.lsp4j.DocumentRangeFormattingParams;
import org.eclipse.lsp4j.DocumentSymbol;
import org.eclipse.lsp4j.DocumentSymbolParams;
import org.eclipse.lsp4j.Hover;
import org.eclipse.lsp4j.HoverParams;
import org.eclipse.lsp4j.Location;
import org.eclipse.lsp4j.LocationLink;
import org.eclipse.lsp4j.PublishDiagnosticsParams;
import org.eclipse.lsp4j.SymbolInformation;
import org.eclipse.lsp4j.TextEdit;
import org.eclipse.lsp4j.jsonrpc.messages.Either;

import io.microshed.jakartals.commons.JakartaDiagnosticsParams;

public class JakartaTextDocumentService implements TextDocumentService {

  private static final Logger LOGGER = Logger.getLogger(JakartaTextDocumentService.class.getName());

  private final JakartaLanguageServer jakartaLanguageServer;

	// Text document manager that maintains the contexts of the text documents
  private final TextDocuments<TextDocument> documents = new TextDocuments<TextDocument>();

  public JakartaTextDocumentService(JakartaLanguageServer jls) {
    this.jakartaLanguageServer = jls;
  }

  @Override
  public void didOpen(DidOpenTextDocumentParams params) {
		TextDocument document = documents.onDidOpenTextDocument(params);
		String uri = document.getUri();
		triggerValidationFor(Arrays.asList(uri));
  }

  @Override
	public void didChange(DidChangeTextDocumentParams params) {
		TextDocument document = documents.onDidChangeTextDocument(params);
		String uri = document.getUri();
		triggerValidationFor(Arrays.asList(uri));
	}

	@Override
	public void didClose(DidCloseTextDocumentParams params) {
		documents.onDidCloseTextDocument(params);
		String uri = params.getTextDocument().getUri();
		jakartaLanguageServer.getLanguageClient()
				.publishDiagnostics(new PublishDiagnosticsParams(uri, new ArrayList<Diagnostic>()));
	}

	@Override
	public void didSave(DidSaveTextDocumentParams params) {
		// validate all opened java files
		triggerValidationForAll();
	}
	
	/**
	 * Validate all opened Java files which belong to a MicroProfile project.
	 *
	 * @param projectURIs list of project URIs filter and null otherwise.
	 */
	private void triggerValidationForAll() {
		List<String> allDocs = documents.all().stream().map(doc -> doc.getUri()).collect(Collectors.toList());
		triggerValidationFor(allDocs);
	}
	
	
	@Override
	public CompletableFuture<Hover> hover(HoverParams params) {
		LOGGER.info("received textDocument/hover request");
		return jakartaLanguageServer.getLanguageClient().getJavaHover(params);
	}


  private void triggerValidationFor(List<String> uris) {
		if (uris.isEmpty()) {
			return;
		}
		JakartaDiagnosticsParams javaParams = new JakartaDiagnosticsParams(uris);
		// TODO: Use settings to see if markdown is supported
		// boolean markdownSupported = sharedSettings.getHoverSettings().isContentFormatSupported(MarkupKind.MARKDOWN);
		// if (markdownSupported) {
		// 	javaParams.setDocumentFormat(DocumentFormat.Markdown);
		// }
		javaParams.setDocumentFormat(DocumentFormat.Markdown);
		jakartaLanguageServer.getLanguageClient().getJavaDiagnostics(javaParams) //
				.thenApply(diagnostics -> {
					if (diagnostics == null) {
						return null;
					}
					for (PublishDiagnosticsParams diagnostic : diagnostics) {
						jakartaLanguageServer.getLanguageClient().publishDiagnostics(diagnostic);
					}
					return null;
				});
	}
}
