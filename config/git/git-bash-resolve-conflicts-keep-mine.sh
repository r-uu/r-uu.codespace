# -------------------------------------------
# 1. Resolve all merge conflicts by taking your version
# -------------------------------------------
git status --porcelain | grep '^UU' | cut -c4- | while IFS= read -r file; do
    git checkout --ours "$file"
    git add "$file"
done

# -------------------------------------------
# 2. Remove all tracked .class files safely
# -------------------------------------------
git ls-files -z | grep -z '\.class$' | while IFS= read -r -d '' file; do
    git rm --cached "$file"
done

# -------------------------------------------
# 3. Remove all tracked 'target/' directories recursively safely
# -------------------------------------------
git ls-files -z | grep -z '/target/' | while IFS= read -r -d '' file; do
    git rm -r --cached "$file"
done

# -------------------------------------------
# 4. Commit merge resolution + removal of build artifacts
# -------------------------------------------
git commit -m "Resolve merge conflicts and remove tracked build artifacts (.class and target/)"

# -------------------------------------------
# 5. Verify everything
# -------------------------------------------
git status