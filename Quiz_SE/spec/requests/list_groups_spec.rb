require "rails_helper"

RSpec.describe "Groups API", :type => :request do
  describe "GET /api/groups" do
    before(:each) do
      @user = create :user
      create :group, title: "CSE", group_name: "Hello", instructor: @user
    end

    it "should be successful" do
      get "/api/groups"
      expect(response.status).to eq 200
    end

    it "returns user groups only" do
      get "/api/groups?user_id=#{@user.id}"

      body = JSON.parse(response.body)
      titles = body.map { |m| m["title"] }

      expect(titles).to match_array(["CSE"])
    end

    it "returns no users with no user params" do
      get "/api/groups"

      body = JSON.parse(response.body)
      titles = body.map { |m| m["title"] }

      expect(titles).to match_array([])
    end
  end

  describe "GET /api/groups/:id" do
    before(:each) do
      @user = create :user
      @group = create :group, title: "CSE", group_name: "Hello", instructor: @user

      get "/api/groups/#{@group.id}"
      @body = JSON.parse(response.body)
    end

    it "should be successful" do
      expect(response.status).to eq 200
    end

    it "returns group title" do
      obj = @body["title"]
      expect(obj).to match(@group.title)
    end

    it "returns group id" do
      obj = @body["id"]
      expect(obj).to match(@group.id)
    end

    it "returns group subject" do
      obj = @body["subject"]
      expect(obj).to match(@group.subject)
    end

    it "returns group year" do
      obj = @body["year"]
      expect(obj).to match(@group.year)
    end

    it "returns group description" do
      obj = @body["description"]
      expect(obj).to match(@group.description)
    end
  end
end