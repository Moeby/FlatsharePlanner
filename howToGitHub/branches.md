## Branches on GitHub

### Bash Commands ([Description](https://github.com/Kunena/Kunena-Forum/wiki/Create-a-new-branch-with-git-and-manage-branches))
```
// create new branch
git checkout -b [name_of_your_new_branch]
// see branches (the one with a star is the one you are working on)
git branch
// change working branch
git checkout [name_of_your_new_branch]
// push to branch
git push origin [name_of_your_new_branch]
// delete branch on local filesystem
git branch -d [name_of_your_new_branch]
// delete branch on github
git push origin :[name_of_your_new_branch]
```

### Theorie
#### What is a branch?
A branch is a workspace which don't affect the master branch (the branch which is ready to deploy).
In your own branch, you can try out new ideas. It won't be merged into the master branch until it's ready to get reviewed.
Therefore, you can delete all your changes by deleting your branch if the are not usable.

#### Flow ([Description](https://guides.github.com/introduction/flow/))
- pull master branch
- create a branch
- add commits
- open pull request
- discuss and review code
- deploy
- merge

#### Branch names
- descriptive, so that others can see what is being worked on

#### Commits in branch
- add commits like you do in master branch

#### Pull Request
- initiate discussion about added commits
- everyone can see what changes would be merged if your request would be accepted
- use @mention to ask for feedback
- you can continue to push new changes to your branch
- will get a number as well as the issues

##### Reasons for a Pull Request
- share some general ideas or screenshots
- you stuck and need help or advice
- work is ready for review

#### Deploy
- deploy your changes to production
- if it causes issues, you can roll it back

#### Merge
- merge your code into the master branch to finally add it to the deployable code
