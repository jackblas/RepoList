# RepoList
An Android app that displays a list of repositories for any GitHub user or organization.

The app contains three activities:
1.	Main Activity – recently looked up users
2.	List Activity – a list of repositories for a selected user
3.	Details Activity – a detail view of the repository

Repositories in the List Activity are grouped by language. Languages with the most repositories are listed first. Within each language group, repositories are sorted in descending order by the stargazers count.

Search results are cached in the local database, to make app usable offline.
