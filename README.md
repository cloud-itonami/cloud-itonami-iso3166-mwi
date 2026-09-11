# cloud-itonami-iso3166-mwi

**MWI**: Malawi.

- PPDA (Public Procurement and Disposal of Public Assets Authority) /
  Public Procurement and Disposal of Public Assets Act, No. 7 of 2025
  public-procurement compliance
- Companies Registration and Intellectual Property Centre (CRIPC --
  successor, since 1 April 2026, to the former Registrar General's
  Department) business/company registration + Malawi Revenue
  Authority (MRA) TPIN registration
- PPDA's own s.62(11) 60%/40% national-competitive-bidding Micro,
  Small and Medium Enterprise (MSME) procurement set-aside eligibility
  gate

AGPL-3.0-or-later.

## Market-entry / statute catalogs

Governed public-sector market-entry compliance actor, same architecture
as every `cloud-itonami-iso3166-*` sibling in this fleet:

- `src/marketentry/{facts,governor,phase,sim,operation,registry,store,
  marketentryllm}.cljc` -- the actor. `facts.cljc` cites the Public
  Procurement and Disposal of Public Assets Authority (PPDA, Act No. 7
  of 2025 -- an Act this iteration downloaded and read directly, and
  which itself honestly documents a 2024-vs-2025 citation-year
  discrepancy on its own face), the Companies Registration and
  Intellectual Property Centre (CRIPC, which absorbed the former
  Registrar General's Department on 1 April 2026) for business/company
  registration under the Companies Act, 2013 and the Business
  Registration Act, and the Malawi Revenue Authority (MRA, Malawi
  Revenue Authority Act, Chapter 39:07) for TPIN registration.
  `governor.cljc`'s flagship check independently recomputes whether an
  engagement's own declared size (annual turnover, employee count,
  assets) satisfies AT LEAST TWO of the THREE size-band criteria
  (turnover / employees / assets) its own declared MSME category
  requires, per the Public Procurement and Disposal of Public Assets
  (Participation by Micro, Small and Medium Enterprises) Order, 2020 --
  a majority-vote multi-axis band-membership test, a check shape
  genuinely different from every other iso3166 sibling's (see the
  namespace docstrings for the full research trail and honestly-
  narrowed scope, including facts this iteration could NOT verify,
  such as the general 'Micro, Small and Medium Enterprises Act, 2024'
  referenced by the 2025 PPDA Act's own interpretation section, and a
  Malawi-specific representative/director exclusion-extension
  provision).
- `src/statute/facts.cljk` -- general-law catalog: the Employment Act
  (Chapter 55:01, Ministry-independent primary text confirmed directly
  via MalawiLII), the Value Added Tax Act (Chapter 42:02, with an
  honestly-flagged K10,000,000-vs-MK25,000,000 registration-threshold
  discrepancy between the Act's own consolidated text and MITC's own
  2026 Investment Guide), and the Companies Act, 2013 (No. 15 of 2013)
  -- unlike the Gambia (a common-law jurisdiction whose own GMB catalog
  carries no Companies Act citation, an honest gap), Malawi DOES have
  an independently-confirmed, current Companies Act.

Every citation is curl/WebFetch-verified against an official source
(ppda.mw, cripc.gov.mw, malawilii.org, mitc.mw); MRA's own site
(mra.mw) is a client-side-rendered React application whose TPIN-
procedure text returned only an empty `<div id="root">` to both `curl`
and `WebFetch` this iteration -- an honestly-flagged ACCESS gap, not a
claim of non-existence, see `marketentry.facts`'s docstring. The
Employment Act, Companies Act 2013, Business Registration Act, Malawi
Revenue Authority Act, Taxation Act and Value Added Tax Act PDFs were
all downloaded directly from MalawiLII and independently confirmed to
be genuine, machine-readable primary legal texts (the Companies Act
2013 is a scanned/OCR'd reproduction with imperfect OCR quality in
places but a legible, genuine citation clause).

## Culture catalog

Alongside the market-entry / statute catalogs, this repo carries a
**country-level regional-culture catalog** (ADR-2607171400 addendum 2,
`cloud-itonami-municipality-culture-catalog` Wave 1, in
`com-junkawasaki/root`) — national dishes, protected products, beverages,
crafts, festivals and heritage sites for Malawi:

- `src/culture/facts.cljk` — the catalog, source of truth (keyed by
  uppercase ISO3, mirroring `statute.facts`).
- `schema/culture.edn` — DataScript schema.
- `data/culture-tx.edn` — derived DataScript tx-data (regenerated from
  the catalog, never hand-edited).

City-level counterparts live in the `cloud-itonami-municipality-*` repos.
Same provenance discipline as the compliance catalogs: every entry cites a
source URL that was actually fetched and read on `:culture/retrieved-at`;
summaries state only what the cited source confirms. An item not in
`culture.facts/catalog` has no spec-basis — never fabricate one.
