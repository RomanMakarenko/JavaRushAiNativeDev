"use client";

import { useState } from "react";

// Головний екран landing-optimizer-mini: поле введення URL і кнопка аналізу.
export default function Page() {
  const [url, setUrl] = useState("");
  const [report, setReport] = useState<string | null>(null);

  // Надсилання форми запускає аналіз введеної адреси.
  function handleAnalyze() {
    setReport(`Звіт за адресою: ${url}`);
  }

  return (
    <main>
      <h1>Landing Optimizer Mini</h1>
      <input
        aria-label="campaign-url"
        value={url}
        onChange={(e) => setUrl(e.target.value)}
        placeholder="https://example.com"
      />
      <button disabled={!url.trim()} onClick={handleAnalyze}>
        Analyze
      </button>
      {report && <p role="status">{report}</p>}
    </main>
  );
}