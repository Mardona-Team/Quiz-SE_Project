require "rails_helper"

RSpec.describe "Quizzes API", :type => :request do
  before(:each) do
    @user = create :user
    @question_one = create :question, answers: create_list(:answer, 3), right_answer: create(:answer)
    @question_two = create :question, answers: create_list(:answer, 3), right_answer: create(:answer)
    question_three = create :question, answers: create_list(:answer, 3), right_answer: create(:answer)
    @quiz = create :quiz, title: "CSE 251", instructor: @user, questions: [@question_one, @question_two]
    create :quiz, title: "CSE 311", instructor: @user, questions: [question_three]
  end

  describe "GET /api/quizzes" do
    it "should be successful" do
      get "/api/quizzes"
      expect(response.status).to eq 200
    end

    it "returns user quizzes only" do
      get "/api/quizzes?instructor_id=#{@user.id}"

      body = JSON.parse(response.body)
      titles = body.map { |m| m["title"] }

      expect(titles).to match_array(["CSE 251",
                                    "CSE 311"])
    end

    it "returns no users with no instructor params" do
      get "/api/quizzes"

      body = JSON.parse(response.body)
      titles = body.map { |m| m["title"] }

      expect(titles).to match_array([])
    end

    it "returns all quizzes with their published attribute" do
      get "/api/quizzes?instructor_id=#{@user.id}"

      body = JSON.parse(response.body)
      published = body.map { |m| m["published"] }

      expect(published).to match_array(["0", "0"])
    end
  end

  describe "GET /api/quizzes/:id" do
    before(:each) do
      get "/api/quizzes/#{@quiz.id}"
      @body = JSON.parse(response.body)
    end

    it "should be successful" do
      expect(response.status).to eq 200
    end

    it "returns quiz title" do
      obj = @body["title"]
      expect(obj).to match(@quiz.title)
    end

    it "returns quiz id" do
      obj = @body["id"]
      expect(obj).to match(@quiz.id)
    end

    it "returns quiz subject" do
      obj = @body["subject"]
      expect(obj).to match(@quiz.subject)
    end

    it "returns quiz year" do
      obj = @body["year"]
      expect(obj).to match(@quiz.year)
    end

    it "returns quiz description" do
      obj = @body["description"]
      expect(obj).to match(@quiz.description)
    end

    it "returns quiz marks" do
      obj = @body["marks"]
      expect(obj).to match(@quiz.marks)
    end

    it "returns quiz questions" do
      obj = @body["questions"].map { |m| m["title"] }
      expect(obj).to match_array([@question_one.title, @question_two.title])
    end
  end

  describe "GET /api/groups/:id/quizzes" do
    before(:each) do
      @group = create :group, title: "CSE", group_name: "Hello", instructor: @user
      question = create :question, answers: create_list(:answer, 3), right_answer: create(:answer)
      create :quiz, title: "CSE 421", instructor: create(:user), questions: [question]

      get "/api/groups/#{@group.id}/quizzes"
      @body = JSON.parse(response.body)
    end

    it "should be successful" do
      expect(response.status).to eq 200
    end

    it "returns all user quizzes" do
      obj = @body.map { |m| m["title"] }

      expect(obj).to match_array(["CSE 251",
                                  "CSE 311"])
    end

    it "returns all quizzes with their published attribute" do
      obj = @body.map { |m| m["published"] }

      expect(obj).to match_array(["0", "0"])
    end

    it "returns quizzes with published attribute '1' when published on group" do
      question = create :question, answers: create_list(:answer, 3), right_answer: create(:answer)
      quiz = create :quiz, title: "CSE 121", instructor: @user, questions: [question]
      quiz.groups << @group

      get "/api/groups/#{@group.id}/quizzes"
      @body = JSON.parse(response.body)
      obj = @body.map { |m| m["published"] }

      expect(obj).to match_array(["0", "0", "1"])
    end
  end
end