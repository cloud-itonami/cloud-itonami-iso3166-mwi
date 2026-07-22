(ns statute.facts
  "General-law compliance catalog for Malawi (MWI) -- extends this
  repo's existing `marketentry.facts` (public-procurement market-entry
  only, narrow scope) with a second, orthogonal catalog of statutes a
  company operating in this jurisdiction must generally track for
  compliance. Mirrors cloud-itonami-iso3166-jpn/-deu/-bgr/-aze/-alb/
  -arm/-atg/-ben/-btn/-bwa/-caf/-est/-gmb/-lso's `statute.facts`
  (ADR-2607141700, cloud-itonami-compliance-fact-federation).

  Every entry cites an OFFICIAL government-hosted (or MalawiLII-hosted
  primary-legal-text) URL -- never fabricated.

  - Labour law: this iteration downloaded the Employment Act (Chapter
    55:01) directly from MalawiLII (`malawilii.org/akn/mw/act/2000/6/
    eng@2014-12-31/source.pdf`, 30 pages, confirmed genuine
    machine-readable text, NOT a scan). Its own text reads: 'Malawi
    Employment Act Chapter 55:01 Commenced on 1 September 2000 ... An
    Act to establish, reinforce and regulate minimum standards of
    employment ... 1. Citation This Act may be cited as the Employment
    Act.' This iteration also read Section 29 ('Notice of termination
    of contracts') directly, confirming concrete minimum-notice-period
    rules tiered by pay frequency and length of service.
  - Tax law: TWO statutes, both independently downloaded and confirmed
    via MalawiLII. The Taxation Act (Chapter 41:01, own text: 'This Act
    may be cited as the Taxation Act') is Malawi's primary income-tax
    statute. The Value Added Tax Act (Chapter 42:02, own text: 'This
    Act may be cited as the Value Added Tax Act', commenced 12 August
    2005) separately governs VAT; its own consolidated text (as at 31
    December 2014) states the compulsory registration threshold as
    'K10,000,000' turnover per annum and empowers the Minister to
    revise this by Gazette notice. The Malawi Investment and Trade
    Centre's own 2026 Investment Guide (`mitc.mw`, fetched directly,
    genuine MITC-authored PDF) separately states a HIGHER, more current
    'MK25 million' voluntary-registration threshold -- this iteration
    did NOT independently fetch the specific Gazette notice bridging
    these two figures, so BOTH are recorded here with their own
    distinct provenance rather than silently reconciled. Only the
    Value Added Tax Act is catalogued below (as the citable primary
    statute); the Taxation Act is referenced in this docstring for
    completeness but not given a separate `catalog` entry, since this
    vertical's tax entry is scoped to VAT registration specifically.
  - Company/commercial-entity law: this iteration independently
    downloaded the Companies Act, 2013 (No. 15 of 2013) directly from
    MalawiLII (`malawilii.org/akn/mw/act/2013/15/eng@2013-07-26/
    source.pdf`, 218 pages -- a scanned/OCR'd reproduction with
    imperfect OCR quality in places, e.g. the President's name renders
    as 'DK JOYCE BAt-1)A' rather than 'Dr. Joyce Banda', but the
    substantive text, including the citation clause, is genuine and
    legible: 'This Act may be cited as the Companies Act, 2013'). Its
    own cover page reads 'ACT No. 15 of 2013 ... Published 26th July,
    2013', assented to 19 July 2013. Unlike the Gambia (a common-law
    jurisdiction whose GMB catalog carries NO Companies Act citation --
    an honest gap this iteration confirmed by absence), Malawi DOES
    have an independently-confirmed, current Companies Act, administered
    by the Registrar of Companies (now under CRIPC, see
    `marketentry.facts`).

  A law not in this table has NO spec-basis, full stop; extend
  `catalog`, do not invent an id/url.")

(def catalog
  "iso3 -> vector of statute entries. `:statute/url` + `:statute/law-number`
  are the citation the governor requires before any compliance-fact
  proposal referencing this law can commit."
  {"MWI"
   [{:statute/id "mwi.employment-act"
     :statute/title "Employment Act"
     :statute/jurisdiction "MWI"
     :statute/kind :law
     :statute/law-number "Employment Act, Chapter 55:01 (own text, downloaded directly from malawilii.org, 30pp, confirmed genuine machine-readable primary text: 'This Act may be cited as the Employment Act.' Commenced on 1 September 2000)"
     :statute/url "https://malawilii.org/akn/mw/act/2000/6/eng@2014-12-31"
     :statute/url-provenance :official-malawilii-org
     :statute/enacted-date "2000-09-01"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:labor}}
    {:statute/id "mwi.value-added-tax-act"
     :statute/title "Value Added Tax Act"
     :statute/jurisdiction "MWI"
     :statute/kind :law
     :statute/law-number "Value Added Tax Act, Chapter 42:02 (own text, downloaded directly from malawilii.org: 'This Act may be cited as the Value Added Tax Act', commenced 12 August 2005. Own consolidated text as at 31 December 2014 states a K10,000,000 compulsory registration threshold, revisable by Gazette notice; the Malawi Investment and Trade Centre's own 2026 Investment Guide separately states a current MK25 million voluntary-registration threshold -- this iteration did not independently fetch the bridging Gazette notice, both figures recorded honestly with distinct provenance, see namespace docstring)"
     :statute/url "https://malawilii.org/akn/mw/act/2005/7/eng@2014-12-31"
     :statute/url-provenance :official-malawilii-org
     :statute/enacted-date "2005-08-12"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:tax}}
    {:statute/id "mwi.companies-act-2013"
     :statute/title "Companies Act, 2013"
     :statute/jurisdiction "MWI"
     :statute/kind :law
     :statute/law-number "Companies Act, 2013 (No. 15 of 2013, own text, downloaded directly from malawilii.org, 218pp -- a scanned/OCR'd reproduction with imperfect OCR quality in places but a genuine, legible citation clause: 'This Act may be cited as the Companies Act, 2013.' Published 26 July 2013, assented to 19 July 2013 by Dr. Joyce Banda, President)"
     :statute/url "https://malawilii.org/akn/mw/act/2013/15/eng@2013-07-26"
     :statute/url-provenance :official-malawilii-org
     :statute/enacted-date "2013-07-19"
     :statute/retrieved-at "2026-07-23"
     :statute/topic #{:corporate-governance}}]})

(defn spec-basis
  "The jurisdiction's statute vector, or nil -- nil means NO spec-basis
  for that jurisdiction yet."
  [iso3]
  (get catalog iso3))

(defn coverage
  "Honest coverage report, same shape/discipline as `marketentry.facts/coverage`:
  never report a missing jurisdiction as covered."
  ([] (coverage (keys catalog)))
  ([iso3s]
   (let [have (filter catalog iso3s)
         missing (remove catalog iso3s)]
     {:requested (count iso3s)
      :covered (count have)
      :covered-jurisdictions (vec (sort have))
      :missing-jurisdictions (vec (sort missing))
      :note (str "cloud-itonami-iso3166-mwi statute.facts Wave 0 (ADR-2607141700): "
                 (count (get catalog "MWI")) " MWI statute(s) seeded with an "
                 "official citation. Extend `statute.facts/catalog`, "
                 "never fabricate a law-id or URL.")})))

(defn by-topic
  "Statutes for `iso3` tagged with `topic` (e.g. :labor, :tax)."
  [iso3 topic]
  (filterv #(contains? (:statute/topic %) topic) (spec-basis iso3)))
